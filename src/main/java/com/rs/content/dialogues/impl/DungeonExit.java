package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;

public class DungeonExit extends Dialogue {

    // private DungControler dungeon;

    @Override
    public void start() {
        sendPlayerDialogue(SEND_2_TEXT_CHAT, "",
                "This ladder leads back to the surface. You will not be able",
                "to come back to this dungeon if you leave.");

    }

    @Override
    public void run(int interfaceId, int componentId) {
        if (stage == -1) {
            sendPlayerDialogue(SEND_2_LARGE_OPTIONS,
                    "Leave the dungeon and return to the surface?", "Yes.",
                    "No.");
            stage = 0;
        } else if (stage == 0) {
            if (componentId == 2) {
                player.dungeon = null;
            }
            end();
        }
    }

    @Override
    public void finish() {

    }

}
