package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.dialogues.DialogueExpressions;
import com.rs.content.economy.shops.ShopsManager;

/**
 * @author FuzzyAvacado
 */
public class Mandrith extends Dialogue {

    public static final int MANDRITH_ID = 6537;

    @Override
    public void start() {
        sendNPCDialogue(MANDRITH_ID, DialogueExpressions.CALM_TALK.getId(), "How can I help you?");
        stage = 1;
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case 0:
                end();
                break;
            case 1:
                sendOptionsDialogue("CHOOSE AN OPTION:", "Who are you?", "Can I see your PK Point Store?", "Oh, sorry, I thought you were someone else.");
                stage = 2;
                break;
            case 2:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(DialogueExpressions.THINKING.getId(), "Who are you?");
                        stage = 3;
                        break;
                    case OPTION_2:
                        ShopsManager.openShop(player, 34);
                        end();
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(DialogueExpressions.CALM_TALK.getId(), "Oh, sorry, I thought you were someone else.");
                        stage = 25;
                }
                break;
            case 3:
                sendNPCDialogue(MANDRITH_ID, DialogueExpressions.HAPPY_TALKING.getId(), "Why, I'm Mandrith! Inspiration to combatants both mighty and puny!");
                stage = 4;
                break;
            case 4:
                sendPlayerDialogue(DialogueExpressions.CALM_TALK.getId(), "Okay...fair enough");
                stage = 5;
                break;
            case 5:
                sendOptionsDialogue("Select an option", "What do you do here?", "Erm, what's with the outfit?", "I have to go now.");
                stage = 6;
                break;
            case 6:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(DialogueExpressions.THINKING.getId(), "What do you do here?");
                        stage = 7;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(DialogueExpressions.CONFUSED.getId(), "Erm, what's with the outfit?");
                        stage = 23;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(DialogueExpressions.CALM_TALK.getId(), "I have to go now.");
                        stage = 0;
                }
                break;
            case 7:
                sendNPCDialogue(MANDRITH_ID, DialogueExpressions.HAPPY_TALKING.getId(), "I am here to collect ancient artefacts acquired by adventurers in return for some well-deserved money.");
                stage = 8;
                break;
            case 8:
                sendOptionsDialogue("Select an option", "What ancient artefacts?", "That sounds great, goodbye.");
                stage = 9;
                break;
            case 9:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(DialogueExpressions.THINKING.getId(), "What ancient artefacts?");
                        stage = 10;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(DialogueExpressions.CALM_TALK.getId(), "That sounds great, goodbye.");
                        stage = 0;
                }
                break;
            case 10:
                sendNPCDialogue(MANDRITH_ID, DialogueExpressions.HAPPY_TALKING.getId(), "Haha! I can tell you are new to these parts.");
                stage = 11;
                break;
            case 11:
                sendNPCDialogue(MANDRITH_ID, DialogueExpressions.CALM_TALK.getId(), "As the blood of warriors is spilled on the ground, as it once "
                        + "was during the God Wars, relics of that age feel the call of battle and drawn into "
                        + "the rays of the sun once more. If, you happen to come across any of these ancient items,");
                stage = 12;
                break;
            case 12:
                sendNPCDialogue(MANDRITH_ID, DialogueExpressions.CALM_TALK.getId(), "bring them to me or my brother Nastroth in Lumbridge, "
                        + "and we will pay you a fair price for them. We don't accept them noted, though, so remember"
                        + " that. Also, we don't want to buy any weapons or armour.");
                stage = 13;
                break;
            case 13:
                sendOptionsDialogue("Select an option", "You have a brother?", "Why won't you buy weapons or armour?", "That sounds great. Goodbye.");
                stage = 14;
                break;
            case 14:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(DialogueExpressions.THINKING.getId(), "You have a brother?");
                        stage = 15;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(DialogueExpressions.THINKING.getId(), "Why won't you buy weapons or armour?");
                        stage = 19;
                        break;
                    case OPTION_3:
                        sendPlayerDialogue(DialogueExpressions.CALM_TALK.getId(), "That sounds great, goodbye.");
                        stage = 0;
                }
                break;
            case 15:
                sendNPCDialogue(MANDRITH_ID, DialogueExpressions.CALM_TALK.getId(), "Yes, why else would I have referred to him as such?");
                stage = 16;
                break;
            case 16:
                sendPlayerDialogue(DialogueExpressions.CALM_TALK.getId(), "You make a good point.");
                stage = 17;
                break;
            case 17:
                sendOptionsDialogue("Select an Option", "Why won't you buy weapons or armour?", "That sounds great. Goodbye");
                stage = 18;
                break;
            case 18:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(DialogueExpressions.THINKING.getId(), "Why won't you buy weapons or armour?");
                        stage = 19;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(DialogueExpressions.CALM_TALK.getId(), "That sounds great. Goodbye.");
                        stage = 0;
                }
                break;
            case 19:
                sendNPCDialogue(MANDRITH_ID, DialogueExpressions.CALM_TALK.getId(), "They should be used as they were meant to be used, and not traded in for money.");
                stage = 20;
                break;
            case 20:
                sendOptionsDialogue("Select an option", "You have a brother?", "That sounds great. Goodbye");
                stage = 21;
                break;
            case 21:
                switch (componentId) {
                    case OPTION_1:
                        sendNPCDialogue(MANDRITH_ID, DialogueExpressions.CALM_TALK.getId(), "Yes, why else would I have referred to him as such?");
                        stage = 22;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(DialogueExpressions.CALM_TALK.getId(), "That sounds great. Goodbye");
                        stage = 0;
                }
                break;
            case 22:
                sendPlayerDialogue(DialogueExpressions.CALM_TALK.getId(), "You make a good point.");
                stage = 0;
                break;
            case 23:
                sendNPCDialogue(MANDRITH_ID, DialogueExpressions.CALM_TALK.getId(), "You like not my kingly robes? They were my father's, and"
                        + " his father's before him, and his father's before him, and his father's before him, and-");
                stage = 24;
                break;
            case 24:
                sendPlayerDialogue(DialogueExpressions.CALM_TALK.getId(), "Okay! Okay! I get the picture.");
                stage = 0;
                break;
            case 25:
                sendNPCDialogue(MANDRITH_ID, DialogueExpressions.CALM_TALK.getId(), "I'm not sure how you could confuse ME with anyone!");
                stage = 0;
                break;
        }

    }

    @Override
    public void finish() {

    }

}
