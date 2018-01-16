package com.rs.content.dialogues.impl;


import com.rs.content.dialogues.Dialogue;
import com.rs.player.controlers.QueenBlackDragonController;

public class SummoningPortal extends Dialogue {

    @Override
    public void start() {
        sendDialogue("You will be sent to the heart of this cave complex - alone. There is no way out other than victory, teleportation, or death. Only those who can endure dangerous encounters (combat level 120 or more) should proceed.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Fight the Queen Black Dragon?", "Yes.", "No.");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                player.getControllerManager().startController(
                        QueenBlackDragonController.class);
            }
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