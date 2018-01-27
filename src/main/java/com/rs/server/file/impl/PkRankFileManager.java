package com.rs.server.file.impl;

import com.google.gson.reflect.TypeToken;
import com.rs.core.file.impl.PlayerKillingRank;
import com.rs.utils.Logger;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.server.file.JsonFileManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author FuzzyAvacado
 */
@RequiredArgsConstructor
@Getter(AccessLevel.PRIVATE)
public final class PkRankFileManager extends JsonFileManager {

    @Setter(AccessLevel.PRIVATE)
    private List<PlayerKillingRank> pkRanks;

    private final String path;

    public void init() {
        final File file = new File(getPath());
        if (file.exists()) {
            try {
                setPkRanks(load(getPath(), new TypeToken<ArrayList<PlayerKillingRank>>() {
                }.getType()));
                return;
            } catch (final Throwable e) {
                Logger.handle(e);
            }
        }
        setPkRanks(new ArrayList<>(300));
        save();
    }

    public void save() {
        try {
            save(getPath(), getPkRanks());
        } catch (IOException e) {
            Logger.handle(e);
        }
    }

    public void showRanks(final Player player) {
        for (int i = 10; i < 310; i++) {
            player.getPackets().sendIComponentText(275, i, "");
        }
        for (int i = 0; i < getPkRanks().size(); i++) {
            PlayerKillingRank playerKillingRank = getPkRanks().get(i);
            if (playerKillingRank == null) {
                break;
            }
            String text;
            if (i <= 2) {
                text = "<col=ff9900>";
            } else if (i <= 9) {
                text = "<col=ff0000>";
            } else if (i <= 49) {
                text = "<col=38610B>";
            } else {
                text = "<col=000000>";
            }
            player.getPackets().sendIComponentText(275, i + 10, text + "Top " + (i + 1) + " - " + Utils.formatPlayerNameForDisplay(playerKillingRank.getUsername())
                    + " - kills: " + playerKillingRank.getKills()
                    + " - deaths: " + playerKillingRank.getDeaths());
        }
        player.getPackets().sendIComponentText(275, 1,
                "Player Killing Ranks Table");
        player.getInterfaceManager().sendInterface(275);
    }

    public void sort() {
        getPkRanks().sort((arg0, arg1) -> {
            if (arg0 == null)
                return 1;
            if (arg1 == null)
                return -1;
            return Integer.compare(arg1.getKills(), arg0.getKills());
        });
    }

    public void checkRank(final Player player) {
        final int kills = player.getKillCount();
        for (PlayerKillingRank PkRankFileManager : getPkRanks()) {
            int index = getPkRanks().indexOf(PkRankFileManager);
            if (PkRankFileManager == null) {
                break;
            }
            if (PkRankFileManager.getUsername().equalsIgnoreCase(player.getUsername())) {
                getPkRanks().set(index, new PlayerKillingRank(player));
                sort();
                return;
            }
        }
        for (PlayerKillingRank PkRankFileManager : getPkRanks()) {
            int index = getPkRanks().indexOf(PkRankFileManager);
            if (PkRankFileManager == null) {
                getPkRanks().set(index, new PlayerKillingRank(player));
                sort();
                return;
            }
        }
        for (PlayerKillingRank PkRankFileManager : getPkRanks()) {
            int index = getPkRanks().indexOf(PkRankFileManager);
            if (PkRankFileManager.getKills() < kills) {
                getPkRanks().set(index, new PlayerKillingRank(player));
                sort();
                return;
            }
        }
    }
}
