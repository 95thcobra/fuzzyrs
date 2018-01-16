package com.rs.core.utils.tools;

import com.rs.core.cache.Cache;
import com.rs.core.file.DataFile;
import com.rs.core.utils.Utils;
import com.rs.player.Player;

import java.io.File;
import java.io.IOException;

public class UnbanAll {

    public static void main(final String[] args) {
        try {
            Cache.init();
        } catch (final IOException e1) {
            e1.printStackTrace();
        }
        final File dir = new File("./data/characters/");
        final File[] accs = dir.listFiles();
        for (final File acc : accs) {
            final String name = Utils.formatPlayerNameForProtocol(acc.getName()
                    .replace(".p", ""));
            System.out.println(acc);
            if (Utils.containsInvalidCharacter(name)) {
                acc.delete();
                return;
            }
            try {
                final Player player = new DataFile<Player>(acc).fromSerialUnchecked();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}
