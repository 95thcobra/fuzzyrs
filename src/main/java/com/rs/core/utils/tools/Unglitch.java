package com.rs.core.utils.tools;

import com.rs.content.player.PlayerRank;
import com.rs.core.file.DataFile;
import com.rs.core.file.managers.PlayerFilesManager;
import com.rs.player.Player;

import java.io.File;

public class Unglitch {

    /**
     * @param args
     */
    public static void main(final String[] args) throws Exception {
        final File dir = new File("./checkacc/");
        final File[] accs = dir.listFiles();
        for (final File acc : accs) {
            final Player player = new DataFile<Player>(acc).fromSerialUnchecked();
            if (player.getRank() != PlayerRank.PLAYER) {
                player.setPermBanned(false);
                player.setBanned(0);
                System.out.println(player.getUsername());
                PlayerFilesManager.savePlayer(player);
            }
        }
    }

}
