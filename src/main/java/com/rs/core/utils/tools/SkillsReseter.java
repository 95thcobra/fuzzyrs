package com.rs.core.utils.tools;

import com.rs.core.file.DataFile;
import com.rs.core.file.managers.PlayerFilesManager;
import com.rs.core.settings.GameConstants;
import com.rs.player.Player;
import com.rs.world.item.Item;

import java.io.File;
import java.io.IOException;

public class SkillsReseter {

    public static void main(final String[] args) {
        final File[] chars = new File(GameConstants.DATA_PATH + "/playersaves/reset").listFiles();
        for (final File acc : chars) {
            try {
                final Player player = new DataFile<Player>(acc).fromSerialUnchecked();
                for (int i = 0; i < 25000; i++) {
                    player.getBank().removeItem(i);
                }
                for (int i = 0; i < 25000; i++) {
                    player.getInventory().getItems()
                            .removeAll(new Item(i, Integer.MAX_VALUE));
                }
                for (int i = 0; i < 25000; i++) {
                    player.getEquipment().getItems()
                            .removeAll(new Item(i, Integer.MAX_VALUE));
                }
                player.setMoneyPouchValue(0);
                player.reseted = 1;
                PlayerFilesManager.savePlayer(player);
            } catch (final Throwable e) {
                e.printStackTrace();
                System.out.println("failed: " + acc.getName());
            }
        }
        System.out.println("Done.");
    }
}
