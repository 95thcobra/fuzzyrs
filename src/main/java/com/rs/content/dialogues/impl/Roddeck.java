package com.rs.content.dialogues.impl;

import com.rs.server.Server;
import com.rs.content.dialogues.Dialogue;
import com.rs.core.cache.loaders.NPCDefinitions;

public class Roddeck extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendEntityDialogue(
                SEND_1_TEXT_CHAT,
                new String[]{
                        NPCDefinitions.getNPCDefinitions(npcId).name,
                        "Hello adventurer, I'd like to welcome you to " + Server.getInstance().getSettingsManager().getSettings().getServerName() + "! If you need some help, I'm your guy."},
                IS_NPC, npcId, 9827);

    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            stage = 0;
            sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE,
                    "I'd like to have the starter information.",
                    "I'm fine, thank you.");
        } else if (stage == 0) {
            if (componentId == OPTION_1) {
                stage = 1;
                sendPlayerDialogue(9827,
                        "I'd like to have the starter information.");
            } else {
                stage = -2;
                sendPlayerDialogue(9827, "I'm fine, thank you.");
            }
        } else if (stage == 1) {
            stage = 2;
            sendEntityDialogue(
                    SEND_2_TEXT_CHAT,
                    new String[]{
                            NPCDefinitions.getNPCDefinitions(npcId).name,
                            "Okay. In order to start your adventure, you need to earn some money.",
                            "You can thief at those stalls over there."},
                    IS_NPC, npcId, 9827);
        } else if (stage == 2) {
            stage = 3;
            sendEntityDialogue(
                    SEND_2_TEXT_CHAT,
                    new String[]{
                            NPCDefinitions.getNPCDefinitions(npcId).name,
                            "After you've made some money by thieving, you have to buy some",
                            "items in the shops, you can find these by typing '::shops'."},
                    IS_NPC, npcId, 9827);
        } else if (stage == 3) {
            stage = 4;
            sendEntityDialogue(
                    SEND_2_TEXT_CHAT,
                    new String[]{
                            NPCDefinitions.getNPCDefinitions(npcId).name,
                            "Now it's time to get some stats. You can easily train your stats",
                            "by typing '::train'."}, IS_NPC, npcId, 9827);
        } else if (stage == 4) {
            stage = 5;
            sendEntityDialogue(
                    SEND_2_TEXT_CHAT,
                    new String[]{
                            NPCDefinitions.getNPCDefinitions(npcId).name,
                            "Don't worry about dying. When you die in " + Server.getInstance().getSettingsManager().getSettings().getServerName() + " you will keep all",
                            "of your items unless you are in the Wilderness or at the Corporeal Beast."},
                    IS_NPC, npcId, 9827);
        } else if (stage == 5) {
            stage = 6;
            sendEntityDialogue(
                    SEND_2_TEXT_CHAT,
                    new String[]{
                            NPCDefinitions.getNPCDefinitions(npcId).name,
                            "Just one more thing. When you've gained some stats you can teleport",
                            "to several places by using the Quest tab, or by talking to Wizard Edvin at home."},
                    IS_NPC, npcId, 9827);
        } else if (stage == 6) {
            stage = -2;
            sendPlayerDialogue(9827,
                    "Thanks for the info, Roddeck. I'm going to start my adventure now!");
        } else {
            end();
        }

    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub

    }

}
