package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.player.content.Magic;
import com.rs.world.WorldTile;

public class WolfHunter extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(
                npcId,
                9827,
                "We are in need of your help! Relekka is being attacked my wolfs and you need to kill them. Everything they drop is yours.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Do you accept the challenge?",
                    "I will kill them for you!", "Nope, just nope.");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0.0D, new WorldTile(
                        2718, 3666, 0));
                end();
            } else if (componentId == OPTION_2) {
                end();
            }
        }
    }

    @Override
    public void finish() {

    }
}