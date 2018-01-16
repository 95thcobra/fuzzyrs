package com.rs.content.dialogues.impl;

import com.rs.content.actions.skills.Skills;
import com.rs.content.dialogues.Dialogue;
import com.rs.content.minigames.castlewars.CastleWarsConstants;
import com.rs.content.minigames.duel.DuelController;
import com.rs.core.cache.loaders.NPCDefinitions;
import com.rs.core.settings.SettingsManager;
import com.rs.player.content.Magic;
import com.rs.player.controlers.*;
import com.rs.world.WorldTile;

public class ShopsTeleport extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        if (SettingsManager.getSettings().ECONOMY) {
            player.getPackets().sendGameMessage(
                    "Mr.Ex is in no mood to talk to you.");
            end();
            return;
        }
        npcId = (Integer) parameters[0];
        sendEntityDialogue(SEND_2_TEXT_CHAT,
                new String[]{NPCDefinitions.getNPCDefinitions(npcId).name,
                        "Hello adventurer, I can teleport you to a zone with",
                        " shops. Would you like to go?"}, IS_NPC, npcId, 9827);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendEntityDialogue(SEND_1_TEXT_CHAT,
                    new String[]{player.getDisplayName(),
                            "Sure, bring me there."}, IS_PLAYER,
                    player.getIndex(), 9827);
            stage = 1;
        } else if (stage == 1) {
            Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3149,
                    5706, 0));
            end();
        } else if (stage == 2) {
            if (componentId == OPTION_1) {
                teleportPlayer(2905, 5203, 0);
            } else if (componentId == OPTION_2) {
                teleportPlayer(2870, 5363, 2);
            } else if (componentId == OPTION_3) {
                teleportPlayer(2901, 5264, 0);
            } else if (componentId == OPTION_4) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2562,
                        5739, 0));
            } else if (componentId == OPTION_5) {
                stage = 3;
                sendOptionsDialogue("Where would you like to go?",
                        "Duel Arena.", "Gnome Agility.", "Dominion Tower.",
                        "Polypore Dungeon", "More Options");
            }
        } else if (stage == 3) {
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3365,
                        3275, 0));
                player.getControllerManager().startController(DuelController.class);
            } else if (componentId == OPTION_2) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2470,
                        3436, 0));
            } else if (componentId == OPTION_3) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3366,
                        3083, 0));
            } else if (componentId == OPTION_4) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4719,
                        5467, 0));
            } else if (componentId == OPTION_5) {
                stage = 4;
                sendOptionsDialogue("Where would you like to go?",
                        "Magic Bank.", "Multi Area. (PvP)", "Fight Pits.",
                        "Wests(PvP)", "More Options");
            }
        } else if (stage == 4) {
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2538,
                        4715, 0));
            } else if (componentId == OPTION_2) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3240,
                        3611, 0));
                player.getControllerManager().startController(Wilderness.class);
            } else if (componentId == OPTION_3) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4608,
                        5061, 0));
            } else if (componentId == OPTION_4) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2984,
                        3596, 0));
                player.getControllerManager().startController(Wilderness.class);
            } else if (componentId == OPTION_5) {
                stage = 5;
                sendOptionsDialogue("Where would you like to go?",
                        "Easts (PvP)", "BrimHaven", "Corp", "Feldip hills",
                        "More Options");
            }
        } else if (stage == 5) {
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3360,
                        3658, 0));
                player.getControllerManager().startController(Wilderness.class);
            } else if (componentId == OPTION_2) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2709,
                        9464, 0));
            } else if (componentId == OPTION_3) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2966,
                        4383, 2));
            } else if (componentId == OPTION_4) {
                player.getPackets().sendGameMessage("Disabled.");
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2570,
                        2916, 0));
            } else if (componentId == OPTION_5) {
                stage = 6;
                sendOptionsDialogue("Where would you like to go?", "Zamorak",
                        "Armadyl", "Castle Wars", "King Black Dragon",
                        "More Options");
            }
        } else if (stage == 6) {
            if (componentId == OPTION_1) {
                teleportPlayer(2925, 5330, 2);
            } else if (componentId == OPTION_2) {
                teleportPlayer(2838, 5297, 2);
            } else if (componentId == OPTION_3) {
                Magic.sendNormalTeleportSpell(player, 0, 0, CastleWarsConstants.LOBBY);
            } else if (componentId == OPTION_4) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3008,
                        3849, 0));
            } else if (componentId == OPTION_5) {
                sendOptionsDialogue("Where would you like to go?",
                        "Kalphite Queen", "Fight Caves", "Fight Kiln",
                        "Queen Black Dragon", "More Options");
                stage = 7;
            }
        } else if (stage == 7) {
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3226,
                        3108, 0));
            } else if (componentId == OPTION_2) {
                Magic.sendNormalTeleportSpell(player, 0, 0, FightCaves.OUTSIDE);
            } else if (componentId == OPTION_3) {
                Magic.sendNormalTeleportSpell(player, 0, 0, FightKiln.OUTSIDE);
            } else if (componentId == OPTION_4) {
                end();
                if (player.getSkills().getLevelForXp(Skills.SUMMONING) < 60) {
                    player.getPackets()
                            .sendGameMessage(
                                    "You need a summoning level of 60 to go through this portal.");
                    return;
                }
                player.getControllerManager().startController(
                        QueenBlackDragonController.class);
            }
            /*
			 * else if (componentId == 2) teleportPlayer(2838, 5297, 2); else if
			 * (componentId == 3) Magic.sendNormalTeleportSpell(player, 0, 0,
			 * CastleWars.LOBBY); else if (componentId == 4)
			 * Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2647,
			 * 9378, 0));
			 */
            else if (componentId == OPTION_5) {
                sendOptionsDialogue("Where would you like to go?", "Nex.",
                        "Bandos.", "Sara.", "Tormented Demons", "More Options");
                stage = 2;
            }
        }
    }

    private void teleportPlayer(final int x, final int y, final int z) {
        player.setNextWorldTile(new WorldTile(x, y, z));
        player.stopAll();
        player.getControllerManager().startController(GodWars.class);
    }

    @Override
    public void finish() {

    }
}
