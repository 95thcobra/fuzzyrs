package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.core.cache.loaders.NPCDefinitions;

public class QuestSoldier extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendEntityDialogue(SEND_1_TEXT_CHAT,
                new String[]{NPCDefinitions.getNPCDefinitions(npcId).name,
                        "He...ll...o.... my friend, can I help you?"}, IS_NPC,
                npcId, 9827);

    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            stage = 0;
            sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE,
                    "Noob! You're killed by a Troll!",
                    "I'm looking for a certain Map.");
        } else if (stage == 0) {
            if (componentId == OPTION_1) {
                stage = -2;
                sendPlayerDialogue(9827,
                        "Whahahahaha, noob! You got killed by a Troll!");
            } else {
                stage = 1;
                sendPlayerDialogue(9827,
                        "I'm looking for a map to the Pest Queen, do you have any idea?");
            }
        } else if (stage == 1) {
            stage = 2;
            sendEntityDialogue(
                    SEND_2_TEXT_CHAT,
                    new String[]{
                            NPCDefinitions.getNPCDefinitions(npcId).name,
                            "Well, you will probably laugh about it, but...",
                            "that scumbag of a troll stole it from me, he's upstairs."},
                    IS_NPC, npcId, 9827);
        } else if (stage == 2) {
            stage = -2;
            sendPlayerDialogue(9827,
                    "Whahaha! I guess I should kill him to receive the map.");
        } else {
            end();
        }

    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub

    }

}
