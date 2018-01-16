package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.core.cache.loaders.NPCDefinitions;
import com.rs.world.Animation;
import com.rs.world.ForceTalk;
import com.rs.world.Graphics;

public class EasterRing extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        sendEntityDialogue(
                SEND_2_TEXT_CHAT,
                new String[]{NPCDefinitions.getNPCDefinitions(npcId).name,
                        "Psssssst. Hey you, psssst. Got something for you. pssst. Keep it quiet."},
                IS_NPC, npcId, 659);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Want an illegal Easter Ring?", "Give me it",
                    "I don't trust you");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                player.setNextForceTalk(new ForceTalk("Happy Easter Mate!"));
                player.setNextAnimation(new Animation(6111));
                player.setNextGraphics(new Graphics(1390));
                player.getInventory().addItem(7927, 1);
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