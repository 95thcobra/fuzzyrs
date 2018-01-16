package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.minigames.Falconry;
import com.rs.player.content.Magic;
import com.rs.player.controlers.ZombieMinigame;
import com.rs.world.WorldTile;

public class SkillingTeleports extends Dialogue {

    @Override
    public void start() {
        stage = 1;
        sendOptionsDialogue("Skilling Teleports", "Fishing", "Mining",
                "Agility", "Woodcutting", "More Options...");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == 1) {
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2599,
                        3421, 0));
                end();
            }
            if (componentId == OPTION_2) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2600,
                        9557, 0));
                end();
            }
            if (componentId == OPTION_3) {
                sendOptionsDialogue("Agility Teleports", "Gnome Agility",
                        "Barbarian Outpost", "Wilderness Agility");
                stage = 3;
            }
            if (componentId == OPTION_4) {
                sendOptionsDialogue("Woodcutting Teleports", "Woodcutting",
                        "Ivy's");
                stage = 8;
            }
            if (componentId == OPTION_5) {
                stage = 2;
                sendOptionsDialogue("Skilling Teleports", "Runecrafting",
                        "Summoning", "Hunter", "Dungeoneering", "Back...");
            }
        } else if (stage == 2) {
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3040,
                        4842, 0));
                end();
            }
            if (componentId == OPTION_2) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2209,
                        5343, 0));
                end();
            }
            if (componentId == OPTION_4) {
                player.getControllerManager()
                        .startController(ZombieMinigame.class, 1);
                end();
            }
            if (componentId == OPTION_3) {
                sendOptionsDialogue("Hunter Teleports", "Normal Hunting",
                        "Falconry Hunting");
                stage = 4;
            }
            if (componentId == OPTION_5) {
                stage = 1;
                sendOptionsDialogue("Skilling Teleports", "Fishing", "Mining",
                        "Agility", "Woodcutting", "More Options...");
            }
        } else if (stage == 3) {
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2470,
                        3436, 0));
                end();
            }
            if (componentId == OPTION_2) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2552,
                        3563, 0));
                end();
            }
            if (componentId == OPTION_3) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2998,
                        3916, 0));
                end();
            }
        } else if (stage == 4) {
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2526,
                        2916, 0));
                end();
            }
            if (componentId == OPTION_2) {
                if (player.getEquipment().wearingArmour()) {
                    end();
                    player.getPackets().sendGameMessage(
                            "You can't do this while wearing armour");
                } else {
                    player.getControllerManager().startController(Falconry.class, 1);
                    end();
                }
            }
        } else if (stage == 8) {
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3498,
                        3620, 0));
                end();
            }
            if (componentId == OPTION_2) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3218,
                        3500, 0));
                end();
            }
        }
    }

    @Override
    public void finish() {

    }

}