package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.world.WorldTile;

public class QuestStart extends Dialogue {

	/*
     * Authors Ozie/Savions Sw Start of the Dialogue
	 */

    @Override
    public void start() {
        stage = 1;
        if (stage == 1) {
            sendOptionsDialogue("Start the Quest?", "Yes", "No");
            stage = 1;
        }
    }

	/*
	 * The run method
	 */

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == 1) {
			/*
			 * If the player chooces yes
			 */
            if (componentId == OPTION_1) {
                player.getPackets()
                        .sendGameMessage(
                                "<col=00ffff>When you arrive you hear a musical sound.");
                player.setNextWorldTile(new WorldTile(3305, 3129, 0));
                end();
            }
			/*
			 * If the player chooces no
			 */

            if (componentId == OPTION_2) {
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