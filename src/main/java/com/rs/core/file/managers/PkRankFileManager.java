package com.rs.core.file.managers;

import com.google.gson.reflect.TypeToken;
import com.rs.server.Server;
import com.rs.core.file.impl.PlayerKillingRank;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;
import com.rs.core.utils.Utils;
import com.rs.player.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author FuzzyAvacado
 */
public class PkRankFileManager {

    private static final String PATH = GameConstants.DATA_PATH + "/pkRanks.json";

    private static List<PlayerKillingRank> pkRanks;

    public static void init() {
        final File file = new File(PATH);
        if (file.exists()) {
            try {
                pkRanks = Server.getInstance().getJsonFileManager().load(PATH, new TypeToken<ArrayList<PlayerKillingRank>>() {
                }.getType());
                return;
            } catch (final Throwable e) {
                Logger.handle(e);
            }
        }
        pkRanks = new ArrayList<>(300);
        save();
    }

    public static void save() {
        try {
            GameFileManager.storeJsonFile(pkRanks, new File(PATH));
        } catch (IOException e) {
            Logger.handle(e);
        }
    }

    public static void showRanks(final Player player) {
        for (int i = 10; i < 310; i++) {
            player.getPackets().sendIComponentText(275, i, "");
        }
        for (PlayerKillingRank PkRankFileManager : pkRanks) {
            int i = pkRanks.indexOf(PkRankFileManager);
            if (PkRankFileManager == null) {
                break;
            }
            String text;
            if (i >= 0 && i <= 2) {
                text = "<col=ff9900>";
            } else if (i <= 9) {
                text = "<col=ff0000>";
            } else if (i <= 49) {
                text = "<col=38610B>";
            } else {
                text = "<col=000000>";
            }
            player.getPackets().sendIComponentText(275, i + 10, text + "Top " + (i + 1) + " - " + Utils.formatPlayerNameForDisplay(PkRankFileManager.getUsername())
                    + " - kills: " + PkRankFileManager.getKills()
                    + " - deaths: " + PkRankFileManager.getDeaths());
        }
        player.getPackets().sendIComponentText(275, 1,
                "Player Killing Ranks Table");
        player.getInterfaceManager().sendInterface(275);
    }

    public static void sort() {
        Collections.sort(pkRanks, (arg0, arg1) -> {
            if (arg0 == null)
                return 1;
            if (arg1 == null)
                return -1;
            if (arg0.getKills() < arg1.getKills())
                return 1;
            else if (arg0.getKills() > arg1.getKills())
                return -1;
            else
                return 0;
        });
    }

    public static void checkRank(final Player player) {
        final int kills = player.getKillCount();
        for (PlayerKillingRank PkRankFileManager : pkRanks) {
            int index = pkRanks.indexOf(PkRankFileManager);
            if (PkRankFileManager == null) {
                break;
            }
            if (PkRankFileManager.getUsername().equalsIgnoreCase(player.getUsername())) {
                pkRanks.set(index, new PlayerKillingRank(player));
                sort();
                return;
            }
        }
        for (PlayerKillingRank PkRankFileManager : pkRanks) {
            int index = pkRanks.indexOf(PkRankFileManager);
            if (PkRankFileManager == null) {
                pkRanks.set(index, new PlayerKillingRank(player));
                sort();
                return;
            }
        }
        for (PlayerKillingRank PkRankFileManager : pkRanks) {
            int index = pkRanks.indexOf(PkRankFileManager);
            if (PkRankFileManager.getKills() < kills) {
                pkRanks.set(index, new PlayerKillingRank(player));
                sort();
                return;
            }
        }
    }
}
