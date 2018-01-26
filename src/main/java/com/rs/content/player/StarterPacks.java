package com.rs.content.player;

import com.rs.player.Equipment;
import com.rs.player.Player;
import com.rs.world.item.Item;

/**
 * @author John (FuzzyAvacado) on 12/22/2015.
 */
public class StarterPacks {

    public static void giveStarter(Player player, int starterId) {
        String message = "";
        switch (starterId) {
            case 0:
                message = "Melee";
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_CAPE, new Item(6568));
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_WEAPON, new Item(1323));
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_AMULET, new Item(1725));
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_FEET, new Item(3105));
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_HAT, new Item(1153));
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_CHEST, new Item(1115));
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_SHIELD, new Item(8845));
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_LEGS, new Item(1067));
                player.getInventory().addItem(1333, 1);
                player.getInventory().addItem(4587, 1);
                break;

            case 1:
                message = "Mage";
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_CAPE, new Item(10499));
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_WEAPON, new Item(841));
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_AMULET, new Item(1731));
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_FEET, new Item(3105));
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_CHEST, new Item(1135));
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_LEGS, new Item(1099));
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_ARROWS, new Item(882, 500));
                player.getInventory().addItem(861, 1);
                player.getInventory().addItem(892, 500);
                player.getInventory().addItem(10499, 1);
                break;

            case 2:
                message = "Range";
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_CAPE, new Item(2412));
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_WEAPON, new Item(1381));
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_AMULET, new Item(1727));
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_FEET, new Item(3105));
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_CHEST, new Item(577));
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_LEGS, new Item(1011));
                player.getEquipment().getItems()
                        .set(Equipment.SLOT_HAT, new Item(579));
                player.getInventory().addItem(1383, 1);
                player.getInventory().addItem(1385, 1);
                player.getInventory().addItem(1387, 1);
                player.getInventory().addItem(558, 10000);
                player.getInventory().addItem(560, 500);
                break;
        }
        player.getInventory().addItem(995, 100000);
        player.getPackets().sendGameMessage("<col=00ffff>You chose The " + message + " Starter Pack");
        player.getEquipment().refresh();
        player.getAppearance().generateAppearenceData();
        player.getEquipment().refreshAll();
        player.setCompleted(player.getCompleted() + 1);
        player.getAppearance().setTitle(3678);
    }

}
