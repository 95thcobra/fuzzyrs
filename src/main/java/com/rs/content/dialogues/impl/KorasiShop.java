package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.economy.shops.ShopsManager;

public class KorasiShop extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(
                npcId,
                9827,
                "Hello adventurer, after everything you've done for me by killing the Pest Queen, I'm willing to sell you my sword.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Have you completed the Quest?", "Yes", "No");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                ShopsManager.openShop(player, 50);
                end();
            } else {
                player.sendMessage("You must complete the Child's Quest in order to open this Shop.");
            }
            end();
        } else if (componentId == OPTION_2) {
            player.sendMessage("You must complete the Child's Quest in order to open this Shop.");
            end();
        }
    }

    @Override
    public void finish() {

    }
}