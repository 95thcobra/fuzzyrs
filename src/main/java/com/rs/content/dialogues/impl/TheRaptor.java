package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.economy.shops.ShopsManager;

public class TheRaptor extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(
                npcId,
                9827,
                "Hello Pker, I got a store for you with all sorts of items. Would you like to see my Shop?");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Would you like to see the Shop?",
                    "Yes, Please", "No, thank you");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                ShopsManager.openShop(player, 34);
                end();
            } else if (componentId == OPTION_2) {
                end();
            }
        }
    }

    @Override
    public void finish() {

    }
}