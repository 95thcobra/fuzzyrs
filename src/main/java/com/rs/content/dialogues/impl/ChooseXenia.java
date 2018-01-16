package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;

public class ChooseXenia extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(npcId, 9827,
                "I'm a NPC with two dialogues. Click on one of the following options.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Choose a dialogue", "Name colour dialogue",
                    "Skin changing dialogue");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                player.getDialogueManager().startDialogue(ChangeColour.class, 9634);
            } else if (componentId == OPTION_2) {
                player.getDialogueManager().startDialogue(ChangeSkin.class, 9634);
            }
        }
    }

    @Override
    public void finish() {

    }
}