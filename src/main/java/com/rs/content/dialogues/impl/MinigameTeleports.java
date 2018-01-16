package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.minigames.cagegame.CageGameController;
import com.rs.content.minigames.soulwars.SoulWarsAreaController;
import com.rs.player.content.Magic;
import com.rs.world.WorldTile;

public class MinigameTeleports extends Dialogue {

    @Override
    public void start() {
        stage = 1;
        sendOptionsDialogue("MiniGame Teleports", "Dominion Tower",
                "Fight Caves", "Barrows", "Duel Arena", "More options...");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == 1) {
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3373,
                        3090, 0));
                end();
            }
            if (componentId == OPTION_2) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4613,
                        5129, 0));
                end();
            }
            if (componentId == OPTION_3) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3563,
                        3288, 0));
                end();
            }
            if (componentId == OPTION_4) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3365,
                        3275, 0));
                end();
            }
            if (componentId == OPTION_5) {
                stage = 2;
                sendOptionsDialogue("Minigame Teleports", "Fight Kiln",
                        "Castle Wars", "Warrior Guild", "Livid Farm", "More Options...");
            }
        } else if (stage == 2) {
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4743,
                        5170, 0));
                end();
            }
            if (componentId == OPTION_2) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2443,
                        3090, 0));
                end();
            }
            if (componentId == OPTION_3) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2859,
                        3541, 2));
                end();
            }
            if (componentId == OPTION_4) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2111,
                        3937, 0));
                end();
            }
            if (componentId == OPTION_5) {
                stage = 3;
                sendOptionsDialogue("Minigame Teleports", "Clan wars",
                        "Soul wars", "Fight cage", "Coming soon...", "Back...");
            }
        } else if (stage == 3) {
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2993, 9679, 0));
                end();
            }
            if (componentId == OPTION_2) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1890, 3177, 0));
                end();
            }
            if (componentId == OPTION_3) {
                player.getControllerManager().startController(CageGameController.class);
                end();
            }
            if (componentId == OPTION_4) {
                player.sendMessage("Coming soon...");
                end();
            }
            if (componentId == OPTION_5) {
                stage = 1;
                sendOptionsDialogue("MiniGame Teleports", "Dominion Tower",
                        "Fight Caves", "Barrows", "Duel Arena", "More options...");
            }
        }
    }

    @Override
    public void finish() {

    }
}