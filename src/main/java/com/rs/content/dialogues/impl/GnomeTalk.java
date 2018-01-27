package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.utils.Utils;

public class GnomeTalk extends Dialogue {

    private static final String[] SALUTATIONS = {"Beatiful weather isn't it?",
            "Let's go find treasures!",
            "Travel, travel, travel. We all like to travel.", "Howdy Cowboy!",
            "Let's pop some heads!",
            "Hello, want to hear a gossip? Blink is crazy!"};
    int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(npcId, 9827,
                SALUTATIONS[Utils.random(SALUTATIONS.length)]);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        switch (stage) {
            case -1:
                stage = -2;
                switch (Utils.random(1)) {
                    case 0:
                        end();
                        break;
                    case 1:
                        end();
                        break;
                }
                break;
            default:
                end();
                break;
        }
    }

    @Override
    public void finish() {

    }

}
