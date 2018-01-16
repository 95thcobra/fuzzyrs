package com.rs.content.dialogues.impl;

import com.rs.content.actions.skills.Skills;
import com.rs.content.dialogues.Dialogue;
import com.rs.player.content.Magic;
import com.rs.player.controlers.GodWars;
import com.rs.player.controlers.QueenBlackDragonController;
import com.rs.world.WorldTile;

public class BossTeleports extends Dialogue {

    @Override
    public void start() {
        stage = 1;
        if (stage == 1) {
            sendOptionsDialogue("Pvm Teleports", "Bandos", "Armadyl",
                    "Saradomin", "Zamorak", "More Options...");
            stage = 1;
        }
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == 1) {
            final int option;
            if (componentId == OPTION_1) {
                if (!player.canSpawn()) {
                    player.getPackets()
                            .sendGameMessage(
                                    "<col=ff0000>You can't teleport while you're in this area.</col>");
                    end();
                } else {
                    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(
                            2870, 5363, 0));
                    player.getControllerManager().startController(GodWars.class);
                    end();
                }
            }
            if (componentId == OPTION_2) {
                if (!player.canSpawn()) {
                    player.getPackets()
                            .sendGameMessage(
                                    "<col=ff0000>You can't teleport while you're in this area.</col>");
                    end();
                } else {
                    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(
                            2828, 5302, 0));
                    player.getControllerManager().startController(GodWars.class);
                    end();
                }
            }
            if (componentId == OPTION_3) {
                if (!player.canSpawn()) {
                    player.getPackets()
                            .sendGameMessage(
                                    "<col=ff0000>You can't teleport while you're in this area.</col>");
                    end();
                } else {
                    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(
                            2923, 5250, 0));
                    player.getControllerManager().startController(GodWars.class);
                    end();
                }
            }
            if (componentId == OPTION_4) {
                if (!player.canSpawn()) {
                    player.getPackets()
                            .sendGameMessage(
                                    "<col=ff0000>You can't teleport while you're in this area.</col>");
                    end();
                } else {
                    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(
                            2926, 5325, 0));
                    player.getControllerManager().startController(GodWars.class);
                    end();
                }
            }
            if (componentId == OPTION_5) {
                stage = 2;
                sendOptionsDialogue("Pvm Teleports", "King Black Dragon",
                        "Tormented Demons", "Queen Black Dragon",
                        "Corporeal Beast", "More Options...");
            }
        } else if (stage == 2) {
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3067, 10254, 0));
                player.getPackets().sendGameMessage("<col=ff0000>Make sure you equip an anti-dragonshield with you. You will need it!</col>");
                end();
            }
            if (componentId == OPTION_2) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2562,
                        5739, 0));
                end();
            }
            if (componentId == OPTION_3) {
                end();
                if (player.getSkills().getLevelForXp(Skills.SUMMONING) < 60) {
                    player.getPackets().sendGameMessage("You need a summoning level of 60 to handle this teleport.");
                    return;
                }
                player.getControllerManager().startController(QueenBlackDragonController.class);
            }
            if (componentId == OPTION_4) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2976,
                        4384, 0));
                end();
            }
            if (componentId == OPTION_5) {
                stage = 3;
                sendOptionsDialogue("Pvm Teleports", "Dagganoth Kings",
                        "Yk'lagor the Thunderous", "Blink", "Nomad(Quest)",
                        "More Options...");
            }
        } else if (stage == 3) {
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2900,
                        4449, 0));
                player.getPackets().sendGameMessage("Watch your feet.");
                end();
            }
            if (componentId == OPTION_2) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2844,
                        9636, 0));
                player.getPackets()
                        .sendGameMessage(
                                "<col=ff0000>Jump over the obstacle to fight him!</col>");
                end();
            }
            if (componentId == OPTION_3) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2722,
                        9513, 0));
                player.getPackets().sendGameMessage(
                        "<col=ff0000>Walk east to fight this boss.</col>");
                end();
            }
            if (componentId == OPTION_4) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3081,
                        3476, 0));
                player.getPackets()
                        .sendGameMessage(
                                "<col=ff0000>Enter the portal and investigate the tent inside.</col>");
                end();
            }
            if (componentId == OPTION_5) {
                sendOptionsDialogue("Pvm Teleports", "Avatar", "Wolverine",
                        "Ice Troll King", "Mutant Tarn", "More Options...");
                stage = 4;
            }
        } else if (stage == 4) {
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4568,
                        5092, 0));
                end();
            }
            if (componentId == OPTION_2) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2693,
                        9482, 0));
                end();
            }
            if (componentId == OPTION_3) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3047,
                        9580, 0));
                end();
            }
            if (componentId == OPTION_4) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3209,
                        5481, 0));
                end();
            }
            if (componentId == OPTION_5) {
                sendOptionsDialogue("Pvm Teleports", "Giant Mole", "Giant Roc",
                        "Kraken", "Coming soon", "Back...");
                stage = 5;
            }
        } else if (stage == 5) {
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1759,
                        5198, 0));
                end();
            }
            if (componentId == OPTION_2) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3390,
                        2975, 0));
                player.getPackets()
                        .sendGameMessage(
                                "<col=ff0000>Activate your ranged prayer once the bird shakes his wings.</col>");
                end();
            }
            if (componentId == OPTION_3) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2014,
                        4825, 0));
                end();
            }
            if (componentId == OPTION_4) {
                end();
            }
            if (componentId == OPTION_5) {
                sendOptionsDialogue("Pvm Teleports", "Bandos", "Armadyl",
                        "Saradomin", "Zamorak", "More Options...");
                stage = 1;
            }
        }
    }

    @Override
    public void finish() {

    }

}