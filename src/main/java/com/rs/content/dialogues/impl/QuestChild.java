package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.world.WorldTile;

public class QuestChild extends Dialogue {

    int npcId;

    @Override
    public void start() {
        if (!player.isTalkedWithChild()) {
            sendEntityDialogue(
                    IS_NPC,
                    "Child",
                    6334,
                    9827,
                    "Hello adventurer, I need your help really bad. My parents have been killed by an huge beast. I want you to wreck them for me!");
        } else {
            sendEntityDialogue(IS_NPC, "Child", 6334, 9827,
                    "Your back adventurer, what do you need?");
        }
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        switch (stage) {
            case -1:
                if (!player.isTalkedWithChild()) {
                    stage = 0;
                    sendPlayerDialogue(9827, "Who are you?");
                } else {
                    stage = 8;
                    sendPlayerDialogue(9827,
                            "I might got something interesting for you.");
                }
                break;
            case 0:
                stage = 1;
                sendEntityDialogue(IS_NPC, "Child", 6334, 9827,
                        "I'm a child with anger you will never understand!");
                break;
            case 1:
                stage = 2;
                sendPlayerDialogue(9827,
                        "Well, who or... what do I have to kill for you?");
                break;
            case 2:
                stage = 3;
                sendEntityDialogue(
                        IS_NPC,
                        "Child",
                        6334,
                        9827,
                        "You have to kill the Pest Queen, but there are some things you need to collect first.");
                break;
            case 3:
                sendEntityDialogue(
                        IS_NPC,
                        "Child",
                        6334,
                        9827,
                        "You have to collect 10 noted Potatoes(10) and you have to ask the wounded soldier in Burthope for a map.");
                player.setTalkedWithChild();
                stage = 6;
                break;
            case 4:
                stage = 5;
                sendPlayerDialogue(9827, "Can you show me where i can kill these?");
                break;
            case 5:
                stage = 6;
                sendEntityDialogue(IS_NPC, "Child", 6334, 9827,
                        "Sorry i dont have time today but maybe next time.");
                break;
            case 6:
                stage = 7;
                sendPlayerDialogue(9827, "Okay no problem i'll try find them!");
                break;
            case 7: /* Offical end of Dialogue */
                end();
                break;
            case 8:
                stage = 9;
                sendEntityDialogue(IS_NPC, "Child", 6334, 9827,
                        "Okay, show me what you got.");
                break;
            case 9:
                stage = 10;
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE,
                        "I got your supplies!", "What do I need to collect again?");
                break;
            case 10:
                switch (componentId) {
                    case OPTION_1:
                        stage = 11;
                        sendPlayerDialogue(9827, "I got the supplies you needed!");
                        break;
                    case OPTION_2:
                        stage = 12;
                        sendPlayerDialogue(9827,
                                "What items do I need to collect again?");
                        break;
                }
                break;
            case 11:
                if (player.getInventory().containsItem(8296, 1))
                    if (player.getInventory().containsItem(5439, 10)) {
                        player.getInventory().deleteItem(5439, 10);
                        player.getInventory().deleteItem(8296, 1);
                        sendEntityDialogue(IS_NPC, "Child", 6334, 9827,
                                "Very good, you are a true adventurer! Would you like to go to the Queen now?");
                        stage = 13;
                        break;
                    } else {
                        sendEntityDialogue(
                                IS_NPC,
                                "Child",
                                6334,
                                9827,
                                "You should collect: 10 noted Potatoes(10) and ask the map from the wounded soldier in Falador's Inn.");
                        stage = 7;
                        break;
                    }
            case 12:
                stage = 7;
                sendEntityDialogue(
                        IS_NPC,
                        "Child",
                        6334,
                        9827,
                        "You should collect: 10 noted Potatoes(10) and ask the map from the wounded soldier in Falador's Inn.");
                break;
            case 13:
                stage = 15;
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Yes", "No thanks");
                break;
            case 14:
                stage = 16;
                sendOptionsDialogue("Do you want to change your task for 500k?",
                        "Yes", "No that to much!");
                break;
            case 15:
                switch (componentId) {
                    case OPTION_1:
                        stage = 17;
                        sendPlayerDialogue(9827, "Yes please");
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9827, "No thanks");
                        stage = 18;
                        break;
                }
                break;
            case 16:
                switch (componentId) {
                    case OPTION_1:
                        stage = 19;
                        sendPlayerDialogue(9827, "Yes please");
                        break;
                    case OPTION_2:
                        stage = 20;
                        sendPlayerDialogue(9827, "No that to much!");
                        break;
                }
                break;
            case 17:
                sendEntityDialogue(IS_NPC, "Child", 6334, 9827,
                        "Thanks for the supplies, on our way then!");
                player.setNextWorldTile(new WorldTile(4512, 5588, 0));
                stage = 7;
                break;
            case 18:
                sendEntityDialogue(IS_NPC, "Child", 6334, 9827,
                        "Okay, see you later my adventurer.");
                stage = 7;
                break;
            case 19:
                if (player.getInventory().containsItem(995, 500000)) {
                    player.getInventory().deleteItem(995, 500000);
                    sendEntityDialogue(IS_NPC, "Child", 6334, 9827,
                            "Your new slayertask is to kill "
                                    + player.getTask().getTaskAmount() + " "
                                    + player.getTask().getName().toLowerCase()
                                    + "s..");
                    stage = 7;
                } else {
                    sendEntityDialogue(IS_NPC, "Child", 6334, 9827,
                            "You dont have 500k gold, come back later!");
                    stage = 7;
                }
                break;
            case 20:
                sendEntityDialogue(IS_NPC, "Child", 6334, 9827,
                        "Im the only Slayermaster and i think 500k is a good price but okay.");
                stage = 7;
                break;
            case 21:
                sendEntityDialogue(IS_NPC, "Child", 6334, 9827,
                        "Lmao are you serious, Okay your task is to kill....");
                stage = 22;
                break;
            case 22:
                sendEntityDialogue(IS_NPC, "Child", 6334, 9827,
                        "Your slayertask is to kill "
                                + player.getTask().getTaskAmount() + " "
                                + player.getTask().getName().toLowerCase()
                                + "s.. Please remember your task next time.");
                stage = 23;
                break;
            case 23:
                sendPlayerDialogue(9827, "Sorry Child, I will remember my task...");
                stage = 7;
                break;
        }
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub

    }

}