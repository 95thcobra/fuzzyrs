package com.rs.content.prestige;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.economy.shops.ShopsManager;
import com.rs.core.cache.loaders.NPCDefinitions;

/**
 * @author John (FuzzyAvacado) on 1/6/2016.
 */
public class PrestigeD extends Dialogue {

    private int npcId = 2253;

    @Override
    public void start() {
        sendEntityDialogue(SEND_2_TEXT_CHAT,
                new String[]{NPCDefinitions.getNPCDefinitions(npcId).name,
                        "Hello " + player.getDisplayName() + " Would you like to learn about the Prestige system?"}, IS_NPC, npcId, 9827);
    }

    @Override
    public void run(int interfaceId, int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Would you like to prestige?", "Yes", "No.", "I would like to receive my prestige title please.", "I would like to access the prestige shop please.", "I would like to buy some skill capes.");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                if (player.getEquipment().wearingArmour()) {
                    player.getPackets().sendGameMessage("<col=ff0000>You must remove your armour before you can prestige.");
                    end();
                } else {
                    sendNPCDialogue(npcId, 9827, "The Prestige System allows you to prestige through 25 levels of Prestige, each prestige will reset your combat skills only, so do not worry about loosing your skill levels. Each Prestige will reward you with a new Prestige title and Prestige tokens.");
                    stage = 2;
                }
            } else if (componentId == OPTION_2) {
                end();
            } else if (componentId == OPTION_3) {
                if (player.getPrestigeLevel() == 1) {
                    player.getAppearance().setTitle(1024);
                } else if (player.getPrestigeLevel() == 2) {
                    player.getAppearance().setTitle(1214);
                } else if (player.getPrestigeLevel() == 3) {
                    player.getAppearance().setTitle(1594);
                } else if (player.getPrestigeLevel() == 4) {
                    player.getAppearance().setTitle(1432);
                } else if (player.getPrestigeLevel() == 5) {
                    player.getAppearance().setTitle(1342);
                } else {
                    player.getPackets().sendGameMessage("You need to have a higher prestige level in order to gain a prestige title.");
                }
                end();
            } else if (componentId == OPTION_4) {
                Prestige.prestigeShops(player);
                end();
            } else if (componentId == OPTION_5) {
                ShopsManager.openShop(player, 18);
                end();
            }
        } else if (stage == 2) {
            sendPlayerDialogue(9827, "Wow sounds amazing!");
            stage = 3;
        } else if (stage == 3) {
            sendNPCDialogue(npcId, 9827, "That's because it is! When you have prestiged you will be rewarded with a new title, bonus xp gains, and the ability to use various Prestige shops!");
            stage = 4;
        } else if (stage == 4) {
            sendOptionsDialogue("Would you like to prestige?", "Yes Please!", "No thank you.");
            stage = 5;
        } else if (stage == 5) {
            if (componentId == OPTION_1) {
                Prestige.canPrestige(player);
            } else if (componentId == OPTION_2) {
                end();
            }
            end();
        }
    }


    @Override
    public void finish() {

    }
}
