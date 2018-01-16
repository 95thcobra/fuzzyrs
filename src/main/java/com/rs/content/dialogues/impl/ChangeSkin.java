package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;

public class ChangeSkin extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(
                npcId,
                9827,
                "Hello adventurer, I'm offering you a chance to change your skin colour. Pick one if you'd like to!");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Choose A Color!", "Blue", "Green");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                player.getAppearance().setSkinColor(12);
            }
            player.getAppearance().generateAppearenceData();
            end();
            if (componentId == OPTION_2) {
                player.getAppearance().setSkinColor(13);
            }
            player.getAppearance().generateAppearenceData();
            end();
        }
    }

    @Override
    public void finish() {

    }
}