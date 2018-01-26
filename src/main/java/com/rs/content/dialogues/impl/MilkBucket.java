package com.rs.content.dialogues.impl;

import com.rs.server.Server;
import com.rs.content.dialogues.Dialogue;
import com.rs.core.cache.loaders.NPCDefinitions;

public class MilkBucket extends Dialogue {

    // was something for when my bday was.

    private int npcId;

    @Override
    public void start() {
        if (Server.getInstance().getSettingsManager().getSettings().isEconomy()) {
            player.getPackets().sendGameMessage(
                    "Mr.Ex is in no mood to talk to you.");
            end();
            return;
        }
        npcId = (Integer) parameters[0];
        sendEntityDialogue(
                SEND_2_TEXT_CHAT,
                new String[]{
                        NPCDefinitions.getNPCDefinitions(npcId).name,
                        "Hello stranger, I see you're interested in milking this cow.",
                        " You need a bucket to milk him though. I can sell you one for 15 gp."},
                IS_NPC, npcId, 9827);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendEntityDialogue(
                    SEND_1_TEXT_CHAT,
                    new String[]{player.getDisplayName(),
                            "That worthless cook could of told me to bring some money."},
                    IS_PLAYER, player.getIndex(), 9827);
            stage = 1;
        } else if (stage == 1) {
            sendOptionsDialogue("Do you want to buy a bucket?",
                    "Yes, give me the bucket.", "No, that's way too expensive.");
            stage = 2;
        } else if (stage == 2) {
            if (componentId == OPTION_1)
                if (player.getInventory().containsItem(995, 15)) {
                    sendNPCDialogue(758, 9827,
                            "Now go milk that cow like you've never done it before!");
                    player.getInventory().deleteItem(995, 15);
                    player.getInventory().addItem(1925, 1);
                } else if (componentId == OPTION_2) {
                    sendNPCDialogue(758, 9827,
                            "I will be here when you've changed your mind.");
                }
            end();
        }
    }

    @Override
    public void finish() {

    }
}
