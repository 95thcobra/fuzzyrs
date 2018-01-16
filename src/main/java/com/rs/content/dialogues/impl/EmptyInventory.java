package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;

public class EmptyInventory extends Dialogue {

    @Override
    public void start() {
        sendDialogue("Are you sure you want to empty your inventory?",
                "There is no way to get your lost items back.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            stage = 0;
            sendOptionsDialogue("Available Options", "Empty Inventory",
                    "Nevermind");
        } else if (stage == 0) {
            if (componentId == OPTION_1) {
                player.getInventory().reset();
            }
            end();
        }

    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub

    }

}
