package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.player.content.RepairItems;

public class Repair extends Dialogue {
    private int itemId;

    @Override
    public void start() {
        itemId = (Integer) parameters[1];
        sendOptionsDialogue("What would you like to do?", "Repair my item!",
                "How much would it cost?", "Nevermind..");

    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        final int amount = player.getInventory().getItems().getNumberOf(itemId);
        if (stage == -1) {
            if (componentId == OPTION_1) {
                if (amount == 1) {
                    RepairItems.Repair(player, itemId, 1);
                    return;
                }
                sendOptionsDialogue("What would you like to do?",
                        "Repair one item.", "Repair X of your items",
                        "Repair all items.");
                stage = 2;
            } else if (componentId == OPTION_2) {
                RepairItems.CheckPrice(player, itemId, amount);
            } else {
                end();
            }

        } else if (stage == 2) {
            if (componentId == OPTION_1) {
                RepairItems.Repair(player, itemId, 1);
            } else if (componentId == OPTION_2) {
                player.getTemporaryAttributtes().put("Repair",
                        Integer.valueOf(0));
                player.getTemporaryAttributtes().put("Ritem", itemId);
                player.getPackets()
                        .sendRunScript(
                                108,
                                "You have "
                                        + amount
                                        + " items that need to be repaired, How many would you like to repair?");
                end();
            } else {
                RepairItems.Repair(player, itemId, amount);
            }
        }
    }

    @Override
    public void finish() {

    }
}