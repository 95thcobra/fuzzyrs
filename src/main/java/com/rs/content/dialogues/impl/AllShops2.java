package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.economy.shops.ShopsManager;

public class AllShops2 extends Dialogue {

    @Override
    public void start() {
        int npcId = (Integer) parameters[0];
        sendNPCDialogue(
                npcId,
                9827,
                "Hey there "
                        + player.getUsername()
                        + ", I'm the Skilling Salesman. I'm willing to sell you my best supplies availible.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            final int option;
            sendOptionsDialogue("What Shop would you like to open?",
                    "Herblore Shop", "Crafting Shop",
                    "Woodcutting & Mining Shop", "Construction Shop");
            stage = 2;
        } else if (stage == 2) {
            if (componentId == OPTION_1) {
                ShopsManager.openShop(player, 20);
                end();
            }
            if (componentId == OPTION_2) {
                ShopsManager.openShop(player, 19);
                end();
            }
            if (componentId == OPTION_3) {
                ShopsManager.openShop(player, 21);
                end();
            }
            if (componentId == OPTION_4) {
                ShopsManager.openShop(player, 36);
                end();
            }
        }
    }

    @Override
    public void finish() {

    }

}