package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.minigames.LucienChest;
import com.rs.player.controlers.DragonHunter;

public class LucienMystery extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(
                npcId,
                9827,
                "Hello adventurer, I'm General Khazard, but I've been slain by my been brother... can ... can you wreck him for me?");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("What would you like to do?",
                    "Start the Minigame", "Receive a reward");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                player.getControllerManager().startController(DragonHunter.class, 1);
                end();
            } else if (componentId == OPTION_2) {
                if (player.getInventory().containsItem(21511, 1)) {
                    LucienChest.searchChest(player);
                } else {
                    player.sendMessage("You need to have a Gold key to receive a random reward.");
                }
                end();
            }
        }
    }

    @Override
    public void finish() {

    }
}