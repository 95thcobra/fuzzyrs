package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.player.content.Magic;
import com.rs.world.WorldTile;

public class PkingTeleports extends Dialogue {

    @Override
    public void start() {
        stage = 1;
        if (stage == 1) {
            sendOptionsDialogue("Pking teleports", "Magic Bank", "Revenants",
                    "Edgeville");
            stage = 1;
        }
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == 1) {
            final int option;
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0D, new WorldTile(
                        2539, 4716, 0));
                end();
            }
            if (componentId == OPTION_2) {
                Magic.sendNormalTeleportSpell(player, 0, 0.0D, new WorldTile(
                        3071, 3649, 0));
                end();
            }
            if (componentId == OPTION_3) {
                Magic.sendNormalTeleportSpell(player, 0, 0.0D, new WorldTile(
                        3087, 3496, 0));
                end();
            }
        }
    }

    @Override
    public void finish() {

    }

}