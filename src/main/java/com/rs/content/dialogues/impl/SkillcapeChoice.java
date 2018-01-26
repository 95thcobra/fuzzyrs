package com.rs.content.dialogues.impl;

import com.rs.server.Server;
import com.rs.content.dialogues.Dialogue;
import com.rs.content.economy.shops.ShopsManager;

public class SkillcapeChoice extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        if (Server.getInstance().getSettingsManager().getSettings().isEconomy()) {
            player.getPackets().sendGameMessage(
                    "Mr.Ex is in no mood to talk to you.");
            end();
            return;
        }
        sendOptionsDialogue("Pick one of the shops", "Skillcape Hoods",
                "Skillcapes");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            if (componentId == OPTION_1) {
                ShopsManager.openShop(player, 55);
            }
            end();
        }
        if (componentId == OPTION_2) {
            ShopsManager.openShop(player, 18);
        }
        end();
    }

    @Override
    public void finish() {

    }
}
