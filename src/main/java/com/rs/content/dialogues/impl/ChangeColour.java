package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;

public class ChangeColour extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(
                npcId,
                9827,
                "Hello adventurer, I'm offering you a chance to change your name colour. Pick one if you'd like to!");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Choose A Color!", "Red", "Blue", "Green",
                    "Pink", "Rainbow");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                player.getAppearance().setTitle(25002);
            }
            end();
            if (componentId == OPTION_2) {
                player.getAppearance().setTitle(25001);
            }
            end();
            if (componentId == OPTION_3) {
                player.getAppearance().setTitle(25003);
            }
            end();
            if (componentId == OPTION_4) {
                player.getAppearance().setTitle(25004);
            }
            end();
            if (componentId == OPTION_5) {
                player.getAppearance().setTitle(25005);
                end();
            }
        }
    }

    @Override
    public void finish() {

    }
}