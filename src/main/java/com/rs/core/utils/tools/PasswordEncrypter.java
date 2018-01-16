package com.rs.core.utils.tools;

import com.rs.core.file.DataFile;
import com.rs.core.file.managers.PlayerFilesManager;
import com.rs.core.utils.file.Encrypt;
import com.rs.player.Player;

import java.io.File;

public class PasswordEncrypter {

    public static void main(final String[] args) {
        encrypt();
    }

    public static void encrypt() {
        final File[] chars = new File("./checkacc/").listFiles();
        for (final File acc : chars) {
            try {
                final Player player = new DataFile<Player>(acc).fromSerialUnchecked();
                if (player == null || player.getPassword() == null) {
                    continue;
                }
                System.out.println(player.getPassword());
                player.setPassword(Encrypt.encryptSHA1(player.getPassword()));
                System.out.println(player.getPassword());
                PlayerFilesManager.savePlayer(player);
            } catch (final Throwable e) {
                System.out.println("failed: " + acc.getName() + ", " + e);
            }
        }
    }
}