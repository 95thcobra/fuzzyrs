package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.economy.shops.ShopsManager;

public class HansShop extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(
                npcId,
                9827,
                "Hello, my friend. I'm Hans and I'm quite a veteran around here. When your Online Time is 10 hours or above you can buy the Veteran Cape.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendNPCDialogue(
                    npcId,
                    9827,
                    "At the moment your Online Time is: "
                            + player.getTime()
                            + " hours. So, have you been online for longer than 10 hours?");
            stage = 1;

        } else if (stage == 1) {
            sendOptionsDialogue("Is your Online Time higher than 10?", "Yes",
                    "No");
            stage = 2;
        } else if (stage == 2) {
            if (componentId == OPTION_1) {
                if (player.getTime() >= 10) {
                    ShopsManager.openShop(player, 52);
                    end();
                } else {
                    player.sendMessage("Sorry, your Online Time must be higher than 10. Your Online Time right now is: "
                            + player.getTime() + ".");
                }
                end();
            } else if (componentId == OPTION_2) {
                player.sendMessage("Sorry, your Online Time must be higher than 10. Your Online Time right now is: "
                        + player.getTime() + ".");
                end();
            }
        }
    }

    @Override
    public void finish() {

    }
}