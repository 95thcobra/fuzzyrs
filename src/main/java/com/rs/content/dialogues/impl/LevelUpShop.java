package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.economy.shops.ShopsManager;

public class LevelUpShop extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(
                npcId,
                9827,
                "Good day "
                        + player.getUsername()
                        + ", I'm the Master of Training. I found quite some items on my journey, would you like to buy some of them?");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            final int option;
            sendOptionsDialogue("What Shop would you like to open?", "Shop 1",
                    "Shop 2", "None");
            stage = 2;
        } else if (stage == 2) {
            if (componentId == OPTION_1) {
                ShopsManager.openShop(player, 37);
                end();
            }
            if (componentId == OPTION_2) {
                ShopsManager.openShop(player, 56);
                end();
            }
            if (componentId == OPTION_3) {
                end();
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