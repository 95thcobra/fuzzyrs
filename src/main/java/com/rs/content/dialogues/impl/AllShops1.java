package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.economy.shops.ShopsManager;

public class AllShops1 extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(
                npcId,
                9827,
                "Hey there "
                        + player.getUsername()
                        + ", I'm the Fishing & Food/Potion Salesman. I'm willing to sell you my best food and potions availible.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            final int option;
            sendOptionsDialogue("What Shop would you like to open?",
                    "Fishing & Food Shop", "Potion Shop");
            stage = 2;
        } else if (stage == 2) {
            if (componentId == OPTION_1) {
                ShopsManager.openShop(player, 22);
                end();
            }
            if (componentId == OPTION_2) {
                ShopsManager.openShop(player, 31);
                end();
            }
        } else if (stage == 3) {
            end();
        }
    }

    @Override
    public void finish() {

    }

}