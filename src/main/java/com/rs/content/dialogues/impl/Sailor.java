package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.world.ForceTalk;
import com.rs.world.WorldTile;

public class Sailor extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(npcId, 9827,
                "*Burp* Arrr matey, I found this new Island. Let's sail together? *Burp*");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Let's sail to this island?", "Let's go",
                    "This can't be good");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                player.setNextForceTalk(new ForceTalk("Arrr Captain!"));
                player.setNextWorldTile(new WorldTile(3795, 2873, 0));
                end();
            } else if (componentId == OPTION_2) {
                player.setNextForceTalk(new ForceTalk("This can't be good."));
                end();
            }
        }
    }

    @Override
    public void finish() {

    }
}