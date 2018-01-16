package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.economy.shops.ShopsManager;

public class MaxShop extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(npcId, 9827,
                "Hello stranger, I can offer you a Shop if you have used the stand once.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Have you used the Stand once?", "Yes", "No");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                if (player.isMaxed == 1) {
                    ShopsManager.openShop(player, 46);
                    end();
                } else {
                    player.sendMessage("You have to use the stand atleast one time.");
                }
                end();
            } else if (componentId == OPTION_2) {
                player.sendMessage("Sorry, you have to use the stand atleast one time.");
                end();
            }
        }
    }

    @Override
    public void finish() {

    }
}