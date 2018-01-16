package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.player.controlers.RunespanController;

public class RunespanPortalD extends Dialogue {

    @Override
    public void start() {
        sendOptionsDialogue("Where would you like to travel to?",
                "The Runecrafting Guild",
                "Low level entrance into the Runespan",
                "High level entrance into the Runespan");
        stage = 1;
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == 1) {
            if (componentId == OPTION_1) {
                player.getPackets().sendGameMessage(
                        "That option isn't yet working.", true);
                end();
            } else {
                player.getPackets().sendGameMessage(
                        "Don't ever go here. Please.", true);
                //RunespanController
                //.enterRunespan(player, componentId == OPTION_3);
                end();
            }
        }

    }

    @Override
    public void finish() {

    }

}
