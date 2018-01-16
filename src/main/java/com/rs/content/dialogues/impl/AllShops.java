package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.economy.shops.ShopsManager;

public class AllShops extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(
                npcId,
                9827,
                "Hey there "
                        + player.getUsername()
                        + ", I'm the Combat Salesman. You can buy almost every item you would except the ones you receive from NPC's or Bosses.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            final int option;
            sendOptionsDialogue("What Shop would you like to open?",
                    "Melee Shop", "Ranged Shop", "Magic Shop", "Armour Shop",
                    "More Options...");
            stage = 2;
        } else if (stage == 2) {
            if (componentId == OPTION_1) {
                ShopsManager.openShop(player, 2);
                end();
            }
            if (componentId == OPTION_2) {
                ShopsManager.openShop(player, 14);
                end();
            }
            if (componentId == OPTION_3) {
                ShopsManager.openShop(player, 10);
                end();
            }
            if (componentId == OPTION_4) {
                ShopsManager.openShop(player, 15);
                end();
            }
            if (componentId == OPTION_5) {
                sendOptionsDialogue("What Shop would you like to open?",
                        "Pking Shop", "Pure Shop", "Misc Shop", "Back...");
                stage = 4;
            }
        } else if (stage == 4) {
            if (componentId == OPTION_1) {
                ShopsManager.openShop(player, 30);
                end();
            }
            if (componentId == OPTION_2) {
                ShopsManager.openShop(player, 27);
                end();
            }
            if (componentId == OPTION_3) {
                ShopsManager.openShop(player, 35);
                end();
            }
            if (componentId == OPTION_4) {
                sendOptionsDialogue("What would you like to open?",
                        "Melee Shop", "Ranged Shop", "Magic Shop",
                        "Armour Shop", "More Options...");
                stage = 2;
            }
        } else if (stage == 3) {
            end();
        }
    }

    @Override
    public void finish() {

    }

}