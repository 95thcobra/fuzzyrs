package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;

public class RepairNex extends Dialogue {

    int npcId;

    @Override
    public void start() {
        sendEntityDialogue(SEND_2_TEXT_CHAT, new String[]{"Nex repair guy",
                "Hello there!", "How can i help you?",}, IS_NPC, 815, 9850);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        switch (stage) {
            case -2:
                end();
                break;
            case -1:
                stage = 2;
                sendPlayerDialogue(9827,
                        "Hello there, I've got a little question for you. Can you fix my Nex armour?");
                break;
            case 2:
                if (player.getInventory().containsItem(20138, 1)
                        || player.getInventory().containsItem(20142, 1)
                        || player.getInventory().containsItem(20146, 1)
                        || player.getInventory().containsItem(20150, 1)
                        || player.getInventory().containsItem(20154, 1)
                        || player.getInventory().containsItem(20158, 1)
                        || player.getInventory().containsItem(20162, 1)
                        || player.getInventory().containsItem(20166, 1)
                        || player.getInventory().containsItem(20174, 1)
                        || player.getInventory().containsItem(20170, 1)) {
                    stage = 5;
                    sendNPCDialogue(519, 9850,
                            "Yes, I can fix anything! Let me take a look.");
                } else {
                    stage = -2;
                    sendNPCDialogue(
                            519,
                            9785,
                            "I see you don't have anything that looks like it belongs to Nex. Come back later.");
                }
                break;
            case 5:
                stage = 6;
                sendNPCDialogue(519, 9836,
                        "It isn't hard to fix, but the materials are worth 50.000.000 per piece.");
                break;
            case 6:
                stage = 7;
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE,
                        "Sure, here you go!", "That is way too much!");
                break;
            case 7:
                if (componentId == OPTION_1) {
                    if (player.getInventory().containsItem(995, 50000000)) {
                        stage = 8;
                        sendPlayerDialogue(9850, "Sure, here you go!");
                    } else {
                        stage = -2;
                        sendPlayerDialogue(
                                9830,
                                "I don't seem to have any money on me at the moment I will be back once I get some.");
                    }
                } else if (componentId == OPTION_2) {
                    stage = -2;
                    sendPlayerDialogue(9785, "That is way too much!");
                }
                break;
            case 8:
                stage = -2;
                sendNPCDialogue(519, 9850, "There you go, good as new!");
                if (player.getInventory().containsItem(20138, 1)
                        && player.getInventory().containsItem(995, 50000000)) {
                    player.getInventory().deleteItem(995, 50000000);
                    player.getInventory().deleteItem(20138, 1);
                    player.getInventory().addItem(20135, 1);
                } else {

                }
                if (player.getInventory().containsItem(20142, 1)
                        && player.getInventory().containsItem(995, 50000000)) {
                    player.getInventory().deleteItem(995, 50000000);
                    player.getInventory().deleteItem(20142, 1);
                    player.getInventory().addItem(20139, 1);
                } else {
                }
                if (player.getInventory().containsItem(20146, 1)
                        && player.getInventory().containsItem(995, 50000000)) {
                    player.getInventory().deleteItem(995, 50000000);
                    player.getInventory().deleteItem(20146, 1);
                    player.getInventory().addItem(20143, 1);
                } else {
                }
                if (player.getInventory().containsItem(20150, 1)
                        && player.getInventory().containsItem(995, 50000000)) {
                    player.getInventory().deleteItem(995, 50000000);
                    player.getInventory().deleteItem(20150, 1);
                    player.getInventory().addItem(20147, 1);
                } else {
                }
                if (player.getInventory().containsItem(20154, 1)
                        && player.getInventory().containsItem(995, 50000000)) {
                    player.getInventory().deleteItem(995, 50000000);
                    player.getInventory().deleteItem(20154, 1);
                    player.getInventory().addItem(20151, 1);
                } else {
                }
                if (player.getInventory().containsItem(20158, 1)
                        && player.getInventory().containsItem(995, 50000000)) {
                    player.getInventory().deleteItem(995, 50000000);
                    player.getInventory().deleteItem(20158, 1);
                    player.getInventory().addItem(20155, 1);
                } else {
                }
                if (player.getInventory().containsItem(20162, 1)
                        && player.getInventory().containsItem(995, 50000000)) {
                    player.getInventory().deleteItem(995, 50000000);
                    player.getInventory().deleteItem(20162, 1);
                    player.getInventory().addItem(20159, 1);
                } else {
                }
                if (player.getInventory().containsItem(20166, 1)
                        && player.getInventory().containsItem(995, 50000000)) {
                    player.getInventory().deleteItem(995, 50000000);
                    player.getInventory().deleteItem(20166, 1);
                    player.getInventory().addItem(20163, 1);
                } else {
                }
                if (player.getInventory().containsItem(20174, 1)
                        && player.getInventory().containsItem(995, 50000000)) {
                    player.getInventory().deleteItem(995, 50000000);
                    player.getInventory().deleteItem(20174, 1);
                    player.getInventory().addItem(20171, 1);
                } else {
                }
                if (player.getInventory().containsItem(20170, 1)
                        && player.getInventory().containsItem(995, 50000000)) {
                    player.getInventory().deleteItem(995, 50000000);
                    player.getInventory().deleteItem(20170, 1);
                    player.getInventory().addItem(20167, 1);
                } else {
                }

                break;

        }
    }

    @Override
    public void finish() {

    }

}