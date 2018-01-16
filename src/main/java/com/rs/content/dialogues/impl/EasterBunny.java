package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;

public class EasterBunny extends Dialogue {

    int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(
                npcId,
                9827,
                "Happy Easter, welcome in my chocolate factory. You have to do something for me.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        switch (stage) {
            case -1:
                stage = 0;
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Okay",
                        "Maybe later");
                break;
            case 0:
                if (componentId == OPTION_2) {
                    stage = 1;
                    sendPlayerDialogue(9827, "Maybe an other time.");
                } else {
                    stage = 2;
                    sendPlayerDialogue(9827, "What do I have to do?");
                }
                break;
            case 1:
                stage = -2;
                sendNPCDialogue(npcId, 9827,
                        "If you change your mind, you know where to find me.");
                break;
            case 2:
                stage = 3;
                sendNPCDialogue(
                        npcId,
                        9827,
                        "Apparently Pernix P has stole Jake his EASTER EGGS! I want you to find them. Can you do that?");
                break;
            case 3:
                stage = 4;
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE,
                        "Everything for the Easter Bunny",
                        "I don't care about Jake");
                break;
            case 4:
                if (componentId == OPTION_2) {
                    end();
                } else {
                    stage = 5;
                    sendPlayerDialogue(9827, "Do you know where to find them?");
                }
                break;
            case 5:
                stage = 6;
                sendNPCDialogue(npcId, 9827, "Yes, he left me some hints.");
                break;
            case 6:
                stage = 7;
                sendNPCDialogue(npcId, 9827,
                        "Hint 1: A place where people are fighting for partyhats.");
                break;
            case 7:
                stage = 8;
                sendNPCDialogue(npcId, 9827,
                        "Hint 2: Near a fountain where everyone is merching.");
                break;
            case 8:
                stage = 9;
                sendNPCDialogue(npcId, 9827,
                        "Hint 3: This is the hard one. It's in one of the boxes in my factory.");
                break;
            case 9:
                stage = 10;
                sendNPCDialogue(
                        npcId,
                        9827,
                        "If you have found the 3 eggs. Give them to Posty Pete, he should be around Lumbridge.");
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
