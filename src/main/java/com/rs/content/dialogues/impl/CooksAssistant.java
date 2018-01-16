package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;

public class CooksAssistant extends Dialogue {

    @Override
    public void start() {
        /*if (!player.isCook()) {
            sendNPCDialogue(278, 9827,
                    "I need your help, " + player.getDisplayName() + "!");
        } else {*/
            sendNPCDialogue(278, 9827, "Do you have the ingredients?");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        switch (stage) {
            case -1:
                /*if (!player.isCook()) {
                    stage = 0;
                    sendPlayerDialogue(9827, "What do you need help with?");
                } else {*/
                    stage = 11;
                    sendPlayerDialogue(9827, "I Do!");
                break;
            case 0:
                sendNPCDialogue(278, 9827,
                        "It's my brother's birthday, and I don't have time "
                                + "to get the ingredients!");
                stage = 1;
                break;
            case 1:
                sendNPCDialogue(278, 9827,
                        "Could you help me " + player.getDisplayName() + "?");
                stage = 2;
                break;
            case 2:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Sure I can!",
                        "Nice hat!", "Sorry, im busy right now.");
            case 3:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9827, "Of course I will help you!");
                        stage = 4;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827,
                                "I love your hat, it matches your outfit.");
                        stage = 5;
                        break;
                    case OPTION_3:
                        stage = 69;
                        sendPlayerDialogue(9827, "Sorry im busy right now.");
                        break;
                }
                break;
            case 4:
                sendNPCDialogue(278, 9827, "Oh Thank you!");
                stage = 6;
                break;
            case 5:
                sendNPCDialogue(278, 9827,
                        "I got it from Thessalia, you should go there");
                stage = 7;
                break;
            case 6:
                sendNPCDialogue(278, 9827, "You will need 3 items for the cake, "
                        + "an egg from the chickens east of here.");
                stage = 8;
                break;
            case 7:
                sendPlayerDialogue(9827, "I've heard they have great prices");
                stage = 69;
                break;
            case 8:
                sendNPCDialogue(278, 9827,
                        "You can get a pot of flour from Fred the farmer, "
                                + "north of here.");
                stage = 9;
                break;
            case 9:
                sendNPCDialogue(278, 9827,
                        "Lastly, you can get a bucket of milk from "
                                + "Gillie Groats. She is with the cows");
                stage = 10;
                break;
            case 10:
                sendNPCDialogue(278, 9827, "Take this pot and give it to Fred");
                player.getInventory().addItem(1931, 1);
                stage = 69;
                break;
            case 11:
                if (player.getInventory().containsItem(1933, 1)
                        && player.getInventory().containsItem(1944, 1)
                        && player.getInventory().containsItem(1927, 1)) {
                    sendNPCDialogue(278, 9827,
                            "Oh thank you " + player.getDisplayName() + "!");
                    stage = 12;
                } else {
                    sendNPCDialogue(278, 9827,
                            "Do not tell me you have the ingredients when you don't! "
                                    + "Be gone from me!");
                    stage = 69;
                }
                break;
            case 12:
                player.getInventory().deleteItem(1933, 1);
                player.getInventory().deleteItem(1944, 1);
                player.getInventory().deleteItem(1927, 1);
                sendNPCDialogue(278, 9827, "I have something to tell you though.");
                stage = 13;
                break;
            case 13:
                sendNPCDialogue(278, 9827, "It's not my brother's Birthday");
                stage = 14;
                break;
            case 14:
                sendPlayerDialogue(9827,
                        "WHAT!? WHY DID YOU MAKE ME GET THOSE INGREDIENTS THEN!?");
                stage = 15;
                break;
            case 15:
                sendNPCDialogue(
                        278,
                        9827,
                        "I had to see if I could trust you, because sooner or later we need to make a halloween meal.");
                stage = 16;
                break;
            case 16:
                sendNPCDialogue(278, 9827,
                        "I need someone I can trust because that Grim Reaper is a though boy...");
                stage = 17;
                break;
            case 17:
                sendNPCDialogue(278, 9827,
                        "...I will reward you for your efforts though");
                stage = 18;
                break;
            case 18:
                player.getInterfaceManager().closeChatBoxInterface();
                player.getInterfaceManager().sendInterface(1244);
                player.getPackets()
                        .sendIComponentText(1244, 25, "Cook's Assistant");
                player.getPackets()
                        .sendIComponentText(1244, 26, "You are awarded:");
                player.getPackets().sendIComponentText(1244, 27,
                        "2 Spins for the Squeal of Fortune");
                player.getPackets().sendIComponentText(1244, 28, "500.000 Coins");
                player.getPackets().sendIComponentText(1244, 23, "Continue");
                player.getPackets().sendItemOnIComponent(1244, 24, 9813, 1);
                player.getInventory().addItem(24155, 1);
                player.getInventory().addItem(995, 500000);
                //player.setDoneCook();
                stage = 69;
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