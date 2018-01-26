package com.rs.server.file.impl;

import com.google.gson.reflect.TypeToken;
import com.rs.core.file.impl.DominionTowerRank;
import com.rs.core.utils.Logger;
import com.rs.core.utils.Utils;
import com.rs.player.DominionTower;
import com.rs.player.Player;
import com.rs.server.file.JsonFileManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author FuzzyAvacado
 */
@Getter(AccessLevel.PRIVATE)
public final class DTRankFileManager extends JsonFileManager {

    private final String path;

    @Setter(AccessLevel.PRIVATE)
    private List<DominionTowerRank> ranks;

    public DTRankFileManager(String path) {
        this.path = path;
    }

    public void init() {
        final File file = new File(getPath());
        if (file.exists()) {
            try {
                setRanks(load(getPath(), new TypeToken<ArrayList<DominionTowerRank>>() {
                }.getType()));
            } catch (IOException e) {
                Logger.handle(e);
            }
            return;
        }
        setRanks(new ArrayList<>(10));
        save();
    }

    public void showRanks(final Player player) {
        player.getInterfaceManager().sendInterface(1158);
        int count = 0;
        for (final DominionTowerRank rank : getRanks()) {
            if (rank == null)
                return;
            player.getPackets().sendIComponentText(1158, 9 + count * 5, Utils.formatPlayerNameForDisplay(rank.getUsername()));
            player.getPackets().sendIComponentText(1158, 10 + count * 5, "On " + (rank.getMode() == DominionTower.CLIMBER ? "climber"
                    : "endurance") + ", reached floor " + rank.getFloorId() + ", killing: " + rank.getBossName() + ".");
            player.getPackets().sendIComponentText(1158, 11 + count * 5, "DF:<br>" + rank.getDominionFactor());
            count++;
        }
    }

    public void save() {
        try {
            save(getPath(), getRanks());
        } catch (IOException e) {
            Logger.handle(e);
        }
    }

    public void sort() {
        getRanks().sort((arg0, arg1) -> {
            if (arg0 == null)
                return 1;
            if (arg1 == null)
                return -1;
            return Long.compare(arg1.getDominionFactor(), arg0.getDominionFactor());
        });
    }

    public void checkRank(final Player player, final int mode, final String boss) {
        final long dominionFactor = player.getDominionTower().getTotalScore();
        for (DominionTowerRank rank : getRanks()) {
            int index = getRanks().indexOf(rank);
            if (rank == null) {
                break;
            }
            if (rank.getUsername().equalsIgnoreCase(player.getUsername())) {
                getRanks().set(index, new DominionTowerRank(player, mode, boss));
                sort();
                return;
            }
        }
        for (DominionTowerRank rank : getRanks()) {
            int index = getRanks().indexOf(rank);
            if (rank == null) {
                getRanks().set(index, new DominionTowerRank(player, mode, boss));
                sort();
                return;
            }
        }
        for (DominionTowerRank rank : getRanks()) {
            int index = getRanks().indexOf(rank);
            if (rank.getDominionFactor() < dominionFactor) {
                getRanks().set(index, new DominionTowerRank(player, mode, boss));
                sort();
                return;
            }
        }
    }
}
