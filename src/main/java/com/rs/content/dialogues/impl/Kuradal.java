package com.rs.content.dialogues.impl;

import com.rs.content.actions.skills.slayer.SlayerTask;
import com.rs.content.actions.skills.slayer.SlayerTask.Master;
import com.rs.content.dialogues.Dialogue;

public class Kuradal extends Dialogue {

    int npcId;

    @Override
    public void start() {
        if (!player.isTalkedWithKuradal()) {
            sendEntityDialogue(IS_NPC, "Kuradal", 9085, 9827,
                    "Hello warrior, What can i do for you?");
        } else {
            sendEntityDialogue(IS_NPC, "Kuradal", 9085, 9827,
                    "Your back warrior... What do you want now?");
        }
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        switch (stage) {
            case -1:
                if (!player.isTalkedWithKuradal()) {
                    stage = 0;
                    sendPlayerDialogue(9827, "Who are you?");
                } else {
                    stage = 8;
                    sendPlayerDialogue(9827,
                            "I would like to ask something about my Task.");
                }
                break;
            case 0:
                stage = 1;
                sendEntityDialogue(IS_NPC, "Kuradal", 9085, 9827,
                        "Im Kuradel! I give out slayer tasks to players around the worldtask.");
                break;
            case 1:
                stage = 2;
                sendPlayerDialogue(9827, "Okay, give me a task then Kuradal!");
                break;
            case 2:
                stage = 3;
                sendEntityDialogue(IS_NPC, "Kuradal", 9085, 9827,
                        "How you know my name?! I guess someone leaked it, Oh well...");
                break;
            case 3:
                if (player.getTask() == null) {
                    SlayerTask.random(player, Master.KURADAL);
                    sendEntityDialogue(IS_NPC, "Kuradal", 9085, 9827,
                            "Your task is to kill "
                                    + player.getTask().getTaskAmount() + " "
                                    + player.getTask().getName().toLowerCase()
                                    + "s..");
                    player.setTalkedWithKuradal();
                    stage = 7;
                } else {
                    sendEntityDialogue(IS_NPC, "Kuradal", 9085, 9827,
                            "You forgot? You already have a slayertask from me! ",
                            "Your task is to kill "
                                    + player.getTask().getTaskAmount() + " "
                                    + player.getTask().getName().toLowerCase()
                                    + "s.");
                    player.setTalkedWithKuradal();
                    stage = 4;
                }
                break;
            case 4:
                stage = 5;
                sendPlayerDialogue(9827, "Can you show me where i can kill these?");
                break;
            case 5:
                stage = 6;
                sendEntityDialogue(IS_NPC, "Kuradal", 9085, 9827,
                        "Sorry i dont have time today but maybe next time.");
                break;
            case 6:
                stage = 7;
                sendPlayerDialogue(9827, "Okay no problem i try find them!");
                break;
            case 7: /* Offical end of Dialogue */
                end();
                break;
            case 8:
                stage = 9;
                sendEntityDialogue(IS_NPC, "Kuradal", 9085, 9827,
                        "Okay, what can i do for you?");
                break;
            case 9:
                stage = 10;
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE,
                        "I have finished my Task!", "Can you change my Task?",
                        "I dont remember my Task.");
                break;
            case 10:
                switch (componentId) {
                    case OPTION_1:
                        stage = 11;
                        sendPlayerDialogue(9827,
                                "I have completed the task you assigned me!");
                        break;
                    case OPTION_2:
                        stage = 12;
                        sendPlayerDialogue(9827,
                                "I would like to have a new task from you...");
                        break;
                    case OPTION_3:
                        stage = 21;
                        sendPlayerDialogue(9827,
                                "Ermmm, i dont really remember the task you gave me.");
                        break;
                }
                break;
            case 11:
                stage = 13;
                if (player.getTask() == null) {
                    sendEntityDialogue(IS_NPC, "Kuradal", 9085, 9827,
                            "Very good, you are a true warrior! Would you like to have a new task?");
                } else {
                    sendEntityDialogue(IS_NPC, "Kuradal", 9085, 9827,
                            "No you haven't foolish warrior.");
                    stage = 7;
                }
                break;
            case 12:
                stage = 14;
                sendEntityDialogue(IS_NPC, "Kuradal", 9085, 9827,
                        "Okay, i can assign a new task for a fee of 500k.");
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
                        stage = 18;
                        sendPlayerDialogue(9827, "No thanks mate.");
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
                SlayerTask.random(player, Master.KURADAL);
                sendEntityDialogue(IS_NPC, "Kuradal", 9085, 9827,
                        "Your new slayertask is to kill "
                                + player.getTask().getTaskAmount() + " "
                                + player.getTask().getName().toLowerCase() + "s..");
                stage = 7;
                break;
            case 18:
                sendEntityDialogue(IS_NPC, "Kuradal", 9085, 9827,
                        "Okay, see you later my warrior.");
                stage = 7;
                break;
            case 19:
                if (player.getInventory().containsItem(995, 500000)) {
                    player.getInventory().deleteItem(995, 500000);
                    SlayerTask.random(player, Master.KURADAL);
                    sendEntityDialogue(IS_NPC, "Kuradal", 9085, 9827,
                            "Your new slayertask is to kill "
                                    + player.getTask().getTaskAmount() + " "
                                    + player.getTask().getName().toLowerCase()
                                    + "s..");
                    stage = 7;
                } else {
                    sendEntityDialogue(IS_NPC, "Kuradal", 9085, 9827,
                            "You dont have 500k gold, come back later!");
                    stage = 7;
                }
                break;
            case 20:
                sendEntityDialogue(IS_NPC, "Kuradal", 9085, 9827,
                        "Im the only Slayermaster and i think 500k is a good price but okay.");
                stage = 7;
                break;
            case 21:
                sendEntityDialogue(IS_NPC, "Kuradal", 9085, 9827,
                        "Lmao are you serious, Okay your task is to kill....");
                stage = 22;
                break;
            case 22:
                sendEntityDialogue(IS_NPC, "Kuradal", 9085, 9827,
                        "Your slayertask is to kill "
                                + player.getTask().getTaskAmount() + " "
                                + player.getTask().getName().toLowerCase()
                                + "s.. Please remember your task next time.");
                stage = 23;
                break;
            case 23:
                sendPlayerDialogue(9827,
                        "Sorry Kuradal, I will remember my task...");
                stage = 7;
                break;
        }
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub

    }

}