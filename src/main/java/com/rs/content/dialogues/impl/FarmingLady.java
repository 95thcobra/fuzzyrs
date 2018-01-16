package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.economy.shops.ShopsManager;

public class FarmingLady extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(
                npcId,
                9827,
                "Hello there "
                        + player.getUsername()
                        + ", I'm a Leprechaun. Im here to sell you farming supplies to achieve the best of the best quality. Would you like to see my Shop?");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            final int option;
            sendOptionsDialogue("Leprechaun", "Buy Farming supplies", "Nothing");
            stage = 2;
        } else if (stage == 2) {
            if (componentId == OPTION_1) {
                ShopsManager.openShop(player, 49);
                end();
            }
            if (componentId == OPTION_3) {
                if (player.getInventory().containsItem(5438, 1)) {
                    player.getInventory().deleteItem(5438, 1);
                    player.getInventory().addItem(995, 50000);
                } else {
                    player.sendMessage("You need to have 1 Potato sack to exchange it for money.");
                }
                end();
            }
            if (componentId == OPTION_2) {
                sendNPCDialogue(npcId, 9827, "Come by later when you need me.");
                stage = 3;
            }
        } else if (stage == 3) {
            end();
        }
    }

    @Override
    public void finish() {

    }

}