package com.rs.content.dialogues.impl;


import com.rs.content.dialogues.Dialogue;

public class ForfeitDialouge extends Dialogue {

    @Override
    public void start() {
        sendOptionsDialogue("Forfeit Duel?", "Yes.", "No.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        switch (componentId) {
            case OPTION_1:
                if (!player.getLastDuelRules().getRule(7)) {
                    sendDialogue("You can't forfeit during this duel.");
                } else {
                    sendDialogue("You can't forfeit during this duel.");
                }
                break;
        }
        end();
    }

    @Override
    public void finish() {

    }

}
