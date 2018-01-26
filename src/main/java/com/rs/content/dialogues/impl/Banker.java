package com.rs.content.dialogues.impl;

import com.rs.Server;
import com.rs.content.dialogues.Dialogue;
import com.rs.core.settings.SettingsManager;

public class Banker extends Dialogue {

    int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(npcId, 9827, "Good day, How may I help you?");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            stage = 0;
            sendOptionsDialogue("What would you like to say?",
                    "I'd like to acess my bank account, please.",
                    "I'd like to check my PIN settings.",
                    "I'd like to remove my PIN.", "What is this place?");
        } else if (stage == 0) {
            if (componentId == OPTION_1) {
                player.getBank().openBank();
                end();
            } else if (componentId == OPTION_2) {
                if (!player.hasBankPin) {
                    stage = 14;
                    sendNPCDialogue(
                            npcId,
                            9827,
                            "Let us set you up with a bank pin. KEEP IN MIND THAT THIS PIN CAN NOT BE RECOVERED. IF YOU FORGET IT YOU WILL NEVER ACCES YOUR BANK AGAIN!");
                }
            } else if (componentId == OPTION_3) {
                // bank pin removal
                if (!player.hasEnteredPin && player.hasBankPin) {
                    player.getTemporaryAttributtes().put("bank_pin1",
                            Boolean.TRUE);
                    player.getPackets().sendRunScript(108,
                            "Enter Your Bank Pin Please");
                    end();
                } else {
                    player.hasBankPin = false;
                    player.getTemporaryAttributtes().put("bank_pin",
                            Boolean.FALSE);
                    player.getPackets().sendGameMessage(
                            "You have removed your bank pin.");
                    end();
                }
            } else if (componentId == OPTION_4) {
                stage = 1;
                sendPlayerDialogue(9827, "What is this place?");
            } else {
                end();
            }
        } else if (stage == 14) {
            player.getTemporaryAttributtes().put("bank_pin", Boolean.TRUE);
            player.getPackets().sendRunScript(108,
                    "Enter a new Bank Pin ");
            stage = -2;
        } else if (stage == 1) {
            stage = 2;
            sendNPCDialogue(npcId, 9827, "This is a branch of the Bank of "
                            + Server.getInstance().getSettingsManager().getSettings().getServerName() + ". We have",
                    "branches in many towns.");
        } else if (stage == 2) {
            stage = 3;
            sendOptionsDialogue("What would you like to say?",
                    "And what do you do?",
                    "Didnt you used to be called the Bank of Varrock?");
        } else if (stage == 3) {
            if (componentId == OPTION_1) {
                stage = 4;
                sendPlayerDialogue(9827, "And what do you do?");
            } else if (componentId == OPTION_2) {
                stage = 5;
                sendPlayerDialogue(9827,
                        "Didnt you used to be called the Bank of Varrock?");
            } else {
                end();
            }
        } else if (stage == 4) {
            stage = -2;
            sendNPCDialogue(npcId, 9827,
                    "We will look after your items and money for you.",
                    "Leave your valuables with us if you want to keep them",
                    "safe.");
        } else if (stage == 5) {
            stage = -2;
            sendNPCDialogue(npcId, 9827,
                    "Yes we did, but people kept on coming into our",
                    "signs were wrong. They acted as if we didn't know",
                    "what town we were in or something.");
        } else {
            end();
        }
    }

    @Override
    public void finish() {

    }

}
