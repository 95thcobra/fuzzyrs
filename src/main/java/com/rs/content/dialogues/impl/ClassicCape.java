package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.core.cache.loaders.NPCDefinitions;

public class ClassicCape extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        sendEntityDialogue(
                SEND_2_TEXT_CHAT,
                new String[]{
                        NPCDefinitions.getNPCDefinitions(npcId).name,
                        "All hail the classic players! I don't know you, but my system does. If you are a classic player you are about to find out."},
                IS_NPC, npcId, 9827);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Want to try it?", "Go for it!", "No");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                if (true) {
                    player.getInventory().addItem(20765, 1);
                    player.getInventory().addItem(20765, 1);
                    end();
                } else {
                    player.sendMessage("You aren't a classic player, I'm sorry.");
                }
            }
        }
        end();
    }

    @Override
    public void finish() {

    }
}