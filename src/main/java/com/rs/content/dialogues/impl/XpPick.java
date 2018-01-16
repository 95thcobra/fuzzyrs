package com.rs.content.dialogues.impl;

import com.rs.content.commands.CommandManager;
import com.rs.content.dialogues.Dialogue;


public class XpPick extends Dialogue {

	/*
     * Authors BongoProd - Rune-Server Start of the Dialogue
	 */

    @Override
    public void start() {
        sendOptionsDialogue("Pick your Xp mode", "Softcore mode (Normal)",
                "Hardcore mode (Special rewards)",
                "Iron man mode (Challenge)");
        stage = 1;
    }

	/*
     * The run method
	 */

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == 1) {
            if (componentId == OPTION_1) {
                CommandManager.processCommand(player, "commands", false, false);
                end();
            }
            if (componentId == OPTION_2) {
                CommandManager.processCommand(player, "commands", false, false);
                end();
            }
            if (componentId == OPTION_3) {
                CommandManager.processCommand(player, "commands", false, false);
                end();
            }
        }

    }

    /*
     * Finishing Method Author Ozie/Savions Sw
     */
    @Override
    public void finish() {

    }

}