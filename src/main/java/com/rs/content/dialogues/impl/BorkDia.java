package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.player.PlayerRank;
import com.rs.player.content.Magic;
import com.rs.world.WorldTile;

public class BorkDia extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(
                npcId,
                9827,
                "Hello stranger, a new threath to the worldtask has been released. But only Extreme Donators have to power to kill him.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Are you an Extreme Donator?", "Yes", "No");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                if (player.getRank().getDonateRank().isMinimumRank(PlayerRank.DonateRank.DONATOR)) {
                    Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(
                            3114, 5528, 0));
                    player.sendMessage("<col=ff000>Only you can kill Bork, but everyone can wear the Celestial Armour!</col>");
                    end();
                } else {
                    player.sendMessage("You aren't an Extreme Donator.");
                }
                end();
            } else if (componentId == OPTION_2) {
                player.sendMessage("Sorry, but this is for Extreme Donators/");
                end();
            }
        }
    }

    @Override
    public void finish() {

    }
}