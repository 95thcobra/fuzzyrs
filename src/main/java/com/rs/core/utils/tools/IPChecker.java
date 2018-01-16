package com.rs.core.utils.tools;

import com.rs.core.file.DataFile;
import com.rs.core.file.GameFileManager;
import com.rs.core.settings.GameConstants;
import com.rs.player.Player;

import java.io.File;
import java.io.IOException;

public class IPChecker {

    public static void main(final String[] args) throws ClassNotFoundException,
            IOException {
        final File[] chars = new File(GameConstants.DATA_PATH + "/characters").listFiles();
        // String ip = args[0];
        for (final File acc : chars) {
            try {
                final Player player = new DataFile<Player>(acc).fromSerialUnchecked();
                if (player == null || player.getMuted() > 0) {
                    continue;
                }
                player.setMuted(0);
                GameFileManager.storeJsonFile(player, acc, true);
            } catch (final Throwable e) {
                System.out.println("failed: " + acc.getName() + ", " + e);
            }
        }
    }

}
