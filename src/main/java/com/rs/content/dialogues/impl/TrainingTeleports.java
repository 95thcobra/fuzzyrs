package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.player.content.Magic;
import com.rs.world.WorldTile;

public class TrainingTeleports extends Dialogue {

    @Override
    public void start() {
        stage = 1;
        if (stage == 1) {
            sendOptionsDialogue("Training teleports", "Yaks (2000 hp)",
                    "Fire Giants (5000 hp)", "Elite Black Knights (Money)",
                    "Dragons (Bones)", "Dwarves (Money)");
            stage = 1;
        }
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == 1) {
            final int option;
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2325,
                        3804, 0));
            }
            if (componentId == OPTION_2) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2577,
                        9881, 0));
            }
            if (componentId == OPTION_3) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3046,
                        9931, 1));
            }
            if (componentId == OPTION_4) {
                sendOptionsDialogue("Dragon Teleports", "Green Dragons",
                        "Blue Dragons", "Frost Dragons");
            }
            stage = 2;
            if (componentId == OPTION_5) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1517,
                        4704, 0));
            }
        }

        if (stage == 2) {
            final int option;
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2979,
                        3604, 0));
            }
            if (componentId == OPTION_2) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2921,
                        9802, 0));
            }
            if (componentId == OPTION_3) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1315,
                        4513, 0));
            }
        }
    }

    @Override
    public void finish() {

    }

}