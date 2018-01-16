package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.player.PlayerRank;

public class VIPCape extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(
                npcId,
                9827,
                "Hey there, I've killed an evil spirit and I achieved his cape. You can have it if you're a V.I.P..");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Are you a V.I.P.?", "Yes I am", "No I'm not");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                if (player.getRank().getDonateRank().isMinimumRank(PlayerRank.DonateRank.EXTREME_DONATOR)) {
                    player.getInventory().addItem(19893, 1);
                    end();
                } else {
                    player.sendMessage("You aren't a V.I.P..");
                }
                end();
            } else if (componentId == OPTION_2) {
                player.sendMessage("Sorry, but this is a V.I.P. only reward.");
                end();
            }
        }
    }

    @Override
    public void finish() {

    }
}