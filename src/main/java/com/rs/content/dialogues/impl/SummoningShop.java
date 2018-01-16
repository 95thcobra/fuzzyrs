package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.economy.shops.ShopsManager;
import com.rs.core.cache.loaders.NPCDefinitions;

public class SummoningShop extends Dialogue {

    @Override
    public void start() {
        stage = 1;
        sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT, new String[]{
                NPCDefinitions.getNPCDefinitions(6970).name,
                "Would you like to see my shops?"}, IS_NPC, 6970, 9847);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == 1) {
            sendOptionsDialogue("Choose a Shop!",
                    "Shop 1 - Basic Ingredients",
                    "Shop 2 - Advanced Ingredients");
            stage = 2;
        } else if (stage == 2) {
            if (componentId == OPTION_1) {
                ShopsManager.openShop(player, 23);
                end();
            }
            if (componentId == OPTION_2) {
                ShopsManager.openShop(player, 24);
                end();
            }
        }
    }

    @Override
    public void finish() {

    }

}