package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;

public class SlaveTalk extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(
                npcId,
                9827,
                "Hello stranger, it's sad to see such a young man imprisoned... If you can kill the Bandit Leader we will reward you.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Have you killed the Bandit Leader?",
                    "Yes, he's death", "No, he's still alive");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                if (player.isMaxed == 1) {
                    player.getInterfaceManager().sendInterface(1244);
                    player.getPackets().sendIComponentText(1244, 25,
                            "The Suppression");
                    player.getPackets().sendIComponentText(1244, 26,
                            "You are awarded:");
                    player.getPackets().sendIComponentText(1244, 27,
                            "Quest Point Cape");
                    player.getPackets()
                            .sendIComponentText(1244, 23, "Continue");
                    player.getPackets().sendItemOnIComponent(1244, 24, 9813, 1);

                    player.sendMessage("Congratulations, you have completed the Quest!");
                    end();
                } else {
                    player.sendMessage("You should kill the Bandit Leader in order to complete this quest.");
                }
                end();
            } else if (componentId == OPTION_2) {
                player.sendMessage("You should kill the Bandit Leader in order to complete this quest.");
                end();
            }
        }
    }

    @Override
    public void finish() {

    }
}