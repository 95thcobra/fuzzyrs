package com.rs.content.dialogues.impl;

import com.rs.content.actions.skills.Skills;
import com.rs.content.dialogues.Dialogue;
import com.rs.player.content.Magic;
import com.rs.world.WorldTile;

public class SlayerTeleports extends Dialogue {

    @Override
    public void start() {
        stage = 1;
        if (stage == 1) {
            sendOptionsDialogue("Slayer Dungeon teleports", "Ice Strykewyrms",
                    "Jadinko Lair", "Polypore Dungeon", "Slayer Tower",
                    "Glacors");
            stage = 1;
        }
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == 1) {
            final int option;
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3421,
                        5662, 0));
            }
            if (componentId == OPTION_2) {
                if (player.getSkills().getLevel(Skills.SLAYER) < 80) {
                    player.getPackets()
                            .sendGameMessage(
                                    "You need an slayer level of 80 to use this teleport.");
                    return;
                }
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3012,
                        9274, 0));
            }
            if (componentId == OPTION_3) {
                if (player.getSkills().getLevel(Skills.SLAYER) < 95) {
                    player.getPackets()
                            .sendGameMessage(
                                    "You need an slayer level of 95 to use this teleport.");
                    end();
                    return;
                }
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4719,
                        5467, 0));
            }
            if (componentId == OPTION_4) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3429,
                        3538, 0));
                end();
            }
            if (componentId == OPTION_5) {
                if (player.getSkills().getLevel(Skills.SLAYER) < 99) {
                    player.getPackets()
                            .sendGameMessage(
                                    "You need an slayer level of 99 to use this teleport.");
                    end();
                    return;
                }
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4184,
                        5732, 0));
            }
        }
    }

    @Override
    public void finish() {

    }

}