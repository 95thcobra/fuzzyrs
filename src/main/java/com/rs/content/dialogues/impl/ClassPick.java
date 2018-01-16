package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.player.StarterPacks;

public class ClassPick extends Dialogue {

	/*
     * Authors Ozie/Savions Sw Start of the Dialogue
	 */

    @Override
    public void start() {
        stage = 1;
        sendOptionsDialogue("Pick your Starter", "The Melee Starter Pack",
                "The Range Starter Pack", "The Mage Starter Pack");
    }

	/*
     * The run method
	 */
    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == 1) {
            if (componentId == OPTION_1) {
                StarterPacks.giveStarter(player, 0);
            }
            if (componentId == OPTION_2) {
                StarterPacks.giveStarter(player, 1);
            }
            if (componentId == OPTION_3) {
                StarterPacks.giveStarter(player, 2);
            }
        }
        end();
        player.getDialogueManager().startDialogue(XpPick.class);
    }

    /*
     * Finishing Method Author Ozie/Savions Sw
     */
    @Override
    public void finish() {

    }

}