package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.core.cache.loaders.NPCDefinitions;
import com.rs.core.settings.SettingsManager;
import com.rs.player.content.Magic;
import com.rs.world.WorldTile;

public class Bday extends Dialogue {

    // was something for when my bday was.

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
        sendEntityDialogue(
                SEND_2_TEXT_CHAT,
                new String[]{
                        NPCDefinitions.getNPCDefinitions(npcId).name,
                        "Hello Adventurer, " + SettingsManager.getSettings().SERVER_NAME + " is back on the track and we",
                        " like to celebrate this, let's keep a party!"},
                IS_NPC, npcId, 9827);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendEntityDialogue(SEND_1_TEXT_CHAT,
                    new String[]{player.getDisplayName(),
                            "Oh my God, that's amazing. I'm part of this!"},
                    IS_PLAYER, player.getIndex(), 9827);
            stage = 1;
        } else if (stage == 1) {
            sendOptionsDialogue("Want to check it out?", "Yes", "No");
            stage = 2;
        } else if (stage == 2) {
            if (componentId == OPTION_1) {
                Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3391,
                        3513, 0));
            } else if (componentId == OPTION_2) {
                end();
            }
        }
    }

    @Override
    public void finish() {

    }
}
