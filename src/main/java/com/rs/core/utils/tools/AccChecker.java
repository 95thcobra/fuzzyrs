package com.rs.core.utils.tools;

import com.rs.core.file.DataFile;
import com.rs.core.file.managers.PlayerFilesManager;
import com.rs.player.Player;

import java.io.File;

public class AccChecker {

    public static void main(final String[] args) {
        final File dir = new File("./checkacc/");
        final File[] accs = dir.listFiles();
        for (final File acc : accs) {
            Player player = new DataFile<Player>(acc).fromSerialUnchecked();
            System.out.println(player.getIPList());
            System.out.println(player.getPasswordList());
            System.out.println(player.getRank().getDonateRank().toString());
            PlayerFilesManager.savePlayer(player);
        }
    }
}