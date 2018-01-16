package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.core.cache.loaders.NPCDefinitions;
import com.rs.player.controlers.trollinvasion.TrollInvasion;
import com.rs.world.WorldTile;

public class Trolly extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendEntityDialogue(
                SEND_2_TEXT_CHAT,
                new String[]{
                        NPCDefinitions.getNPCDefinitions(npcId).name,
                        "Ah, It's Always good to see fresh blood on the ground. You can defend the gatehouse, on Wilkins spot - he's never been quite right... ever since the explosion."},
                IS_NPC, npcId, 9827);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendEntityDialogue(
                    SEND_1_TEXT_CHAT,
                    new String[]{player.getDisplayName(),
                            "Explosion? What? I can't take over for anyone. I don't know what to do!."},
                    IS_PLAYER, player.getIndex(), 9827);
            stage = 1;

        } else if (stage == 1) {
            sendEntityDialogue(
                    SEND_2_TEXT_CHAT,
                    new String[]{
                            NPCDefinitions.getNPCDefinitions(npcId).name,
                            "We all feel like that the first few days, lad. You'll come to grips with it soon enough."},
                    IS_NPC, npcId, 9827);
            stage = 2;

        } else if (stage == 2) {
            sendOptionsDialogue("Avaible Options", "Defend the gatehouse",
                    "Leave");
            stage = 3;

        } else if (stage == 3) {
            if (componentId == OPTION_1) {
                player.getControllerManager().startController(TrollInvasion.class);
            }
            end();
            stage = 4;

        } else if (stage == 4) {
            if (componentId == OPTION_2) {
                end();
            }

        }
    }

    private void teleportPlayer(final int x, final int y, final int z) {
        player.setNextWorldTile(new WorldTile(x, y, z));
        player.stopAll();
    }

    @Override
    public void finish() {

    }
}