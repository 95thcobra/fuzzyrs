package com.rs.core.file.managers;

import com.google.gson.reflect.TypeToken;
import com.rs.Server;
import com.rs.core.file.GameFileManager;
import com.rs.core.file.DataFile;
import com.rs.core.file.impl.DominionTowerRank;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;
import com.rs.core.utils.Utils;
import com.rs.player.DominionTower;
import com.rs.player.Player;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author FuzzyAvacado
 */
public class DTRankFileManager {

    private static final String PATH = GameConstants.DATA_PATH + "/dtRanks.json";

    private static List<DominionTowerRank> ranks;

    public static void init() {
        final File file = new File(PATH);
        if (file.exists()) {
            try {
                ranks = Server.getInstance().getJsonFileManager().load(PATH, new TypeToken<ArrayList<DominionTowerRank>>() {
                }.getType());
            } catch (IOException e) {
                Logger.handle(e);
            }
            return;
        }
        ranks = new ArrayList<>(10);
        save();
    }

    public static void showRanks(final Player player) {
        player.getInterfaceManager().sendInterface(1158);
        int count = 0;
        for (final DominionTowerRank rank : ranks) {
            if (rank == null)
                return;
            player.getPackets().sendIComponentText(1158, 9 + count * 5, Utils.formatPlayerNameForDisplay(rank.getUsername()));
            player.getPackets().sendIComponentText(1158, 10 + count * 5, "On " + (rank.getMode() == DominionTower.CLIMBER ? "climber"
                    : "endurance") + ", reached floor " + rank.getFloorId() + ", killing: " + rank.getBossName() + ".");
            player.getPackets().sendIComponentText(1158, 11 + count * 5, "DF:<br>" + rank.getDominionFactor());
            count++;
        }
    }

    public static void save() {
        try {
            GameFileManager.storeJsonFile(ranks, new File(
                    PATH));
        } catch (IOException e) {
            Logger.handle(e);
        }
    }

    public static void sort() {
        Collections.sort(ranks, (arg0, arg1) -> {
            if (arg0 == null)
                return 1;
            if (arg1 == null)
                return -1;
            if (arg0.getDominionFactor() < arg1.getDominionFactor())
                return 1;
            else if (arg0.getDominionFactor() > arg1.getDominionFactor())
                return -1;
            else
                return 0;
        });
    }

    public static void checkRank(final Player player, final int mode, final String boss) {
        final long dominionFactor = player.getDominionTower().getTotalScore();
        for (DominionTowerRank rank : ranks) {
            int index = ranks.indexOf(rank);
            if (rank == null) {
                break;
            }
            if (rank.getUsername().equalsIgnoreCase(player.getUsername())) {
                ranks.set(index, new DominionTowerRank(player, mode, boss));
                sort();
                return;
            }
        }
        for (DominionTowerRank rank : ranks) {
            int index = ranks.indexOf(rank);
            if (rank == null) {
                ranks.set(index, new DominionTowerRank(player, mode, boss));
                sort();
                return;
            }
        }
        for (DominionTowerRank rank : ranks) {
            int index = ranks.indexOf(rank);
            if (rank.getDominionFactor() < dominionFactor) {
                ranks.set(index, new DominionTowerRank(player, mode, boss));
                sort();
                return;
            }
        }
    }
}
