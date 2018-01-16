package com.rs.content.dialogues.impl;


import com.rs.content.dialogues.Dialogue;

public class Cleanbank extends Dialogue {

    @Override
    public void start() {
        stage = 1;
        if (stage == 1) {
            sendOptionsDialogue("Are you sure you want to reset your bank?",
                    "Yes", "No");
            stage = 1;
        }
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == 1) {
            final int option;
            if (componentId == OPTION_1) {
                player.getBank().getContainer().reset();
            }
            player.getBank().refreshItems();
            end();
            if (componentId == OPTION_2) {
                end();
            }
        }
    }

    @Override
    public void finish() {

    }

}