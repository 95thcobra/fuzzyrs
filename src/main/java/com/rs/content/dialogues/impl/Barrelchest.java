package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.world.ForceTalk;
import com.rs.world.WorldTile;

public class Barrelchest extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(npcId, 9827,
                "Uuuuurgghhhh, thank god an adventurer. Please slay the beast!");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Slay the unknown beast?", "For Freedom!",
                    "You're a pirate anyway");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                player.setNextForceTalk(new ForceTalk("For Freedom!"));
                player.setNextWorldTile(new WorldTile(3725, 2965, 0));
                end();
            } else if (componentId == OPTION_2) {
                player.setNextForceTalk(new ForceTalk("You're a pirate anyway."));
                end();
            }
        }
    }

    @Override
    public void finish() {

    }
}