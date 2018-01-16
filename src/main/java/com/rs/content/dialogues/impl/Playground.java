package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.player.content.Magic;
import com.rs.world.WorldTile;

public class Playground extends Dialogue {

    @Override
    public void start() {
        sendDialogue(
                "Are you sure you want to teleport to Human's Playground?",
                "You are in the wilderness and will lose every item.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            stage = 0;
            sendOptionsDialogue("Available Options", "Teleport me there",
                    "I don't want to lose everything");
        } else if (stage == 0) {
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0D, new WorldTile(
                        3343, 3707, 0));
            }
            end();
        }

    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub

    }

}
