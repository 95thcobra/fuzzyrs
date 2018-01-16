package com.rs.core.utils.tools;

import com.rs.content.player.PlayerRank;
import com.rs.core.file.DataFile;
import com.rs.core.file.managers.IPBanFileManager;
import com.rs.core.file.managers.PlayerFilesManager;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Utils;
import com.rs.player.Player;

import java.io.File;
import java.io.IOException;

public class DIA {

    public static void main(final String[] args) throws ClassNotFoundException,
            IOException {
        IPBanFileManager.init();
        final File[] chars = new File(GameConstants.DATA_PATH + "/characters").listFiles();
        for (final File acc : chars) {
            try {
                final Player player = new DataFile<Player>(acc).fromSerialUnchecked();
                final String name = acc.getName().replace(".p", "");
                player.setUsername(name);
                if (player.getRank() != PlayerRank.PLAYER) {
                    player.setRank(PlayerRank.PLAYER);
                    System.out.println("demoted: " + name);
                    PlayerFilesManager.savePlayer(player);
                }
                if (player.isPermBanned()
                        || player.getBanned() > Utils.currentTimeMillis()) {
                    if (player.getMuted() > Utils.currentTimeMillis()) {
                        player.setMuted(0);
                    }
                    IPBanFileManager.unban(player);
                    System.out.println("unbanned: " + name);
                    PlayerFilesManager.savePlayer(player);
                }
            } catch (final Throwable e) {
                e.printStackTrace();
            }
        }
        System.out.println("Done.");
    }
}
