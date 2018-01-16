package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;

public class Fred extends Dialogue {

    @Override
    public void start() {
        /*if (!player.isCook()) {
            sendNPCDialogue(758, 9827, "What are you looking at?");
        } else {*/
        sendNPCDialogue(758, 9827, "What is it, im busy?");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        switch (stage) {
            case -1:
                /*if (!player.isCook()) {
                    sendPlayerDialogue(9827, "Nothing, sorry");
                    stage = 69;
                } else {*/
                sendPlayerDialogue(9827, "The cook told me to come here.");
                stage = 0;
                break;
            case 0:
                sendNPCDialogue(758, 9827,
                        "You must be that " + player.getDisplayName()
                                + " he told me about");
                stage = 1;
                break;
            case 1:
                sendPlayerDialogue(9827, "Yes, that is me");
                stage = 2;
                break;
            case 2:
                sendPlayerDialogue(9827, "He told me to get flour from you");
                stage = 3;
                break;
            case 3:
                sendNPCDialogue(
                        758,
                        9827,
                        "He did eh? "
                                + "I'll give you flour, but you have to put it in something");
                stage = 4;
                break;
            case 4:
                if (player.getInventory().containsItem(1931, 1)) {
                    sendPlayerDialogue(9827, "I have a pot right here!");
                    stage = 5;
                } else {
                    sendPlayerDialogue(9827, "I lost the pot he gave me!");
                    stage = 6;
                }
                break;
            case 5:
                sendNPCDialogue(758, 9827,
                        "Here you go then, try not to have too much fun with it");
                player.getInventory().deleteItem(1931, 1);
                player.getInventory().addItem(1933, 1);
                stage = 69;
                break;
            case 6:
                sendNPCDialogue(758, 9827, "Ill sell you a pot of flour for 10 gp.");
                stage = 7;
                break;
            case 7:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "That sounds fair",
                        "No way!");
                stage = 8;
                break;
            case 8:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "That sounds fair.");
                        stage = 9;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827,
                                "The cook must not have told you im broke.");
                        stage = 69;
                        break;
                }
                break;
            case 9:
                if (player.getInventory().containsItem(995, 10)) {
                    sendNPCDialogue(758, 9827,
                            "Here you go then, try not to have too much fun with it");
                    player.getInventory().deleteItem(995, 10);
                    player.getInventory().addItem(1933, 1);
                    stage = 69;
                } else {
                    stage = 11;
                    sendNPCDialogue(758, 9827,
                            "Come back when you have more money.");
                    stage = 69;
                }
                break;
            case 69:
                end();
                break;
        }
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub

    }
}