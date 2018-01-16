package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.economy.shops.ShopsManager;
import com.rs.content.player.points.PlayerPoints;
import com.rs.core.settings.SettingsManager;

public class Xuans extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(
                npcId,
                9827,
                "Hey "
                        + player.getUsername()
                        + ", Im Xuan. Im here to sell auras for Loyalty Points, im just telling you this to let you know :) Well what would you like to ask?");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            final int option;
            sendOptionsDialogue("Xuan", "Show me your Shop",
                    "How much points do i have?",
                    "How do i get Loyalty Points?");
            stage = 2;
        } else if (stage == 2) {
            if (componentId == OPTION_1) {
                ShopsManager.openShop(player, 28);
                end();
            }
            if (componentId == OPTION_2) {
                sendNPCDialogue(npcId, 9827,
                        "You currently have " + player.getPlayerPoints().getPoints(PlayerPoints.LOYALTY_POINTS)
                                + " Loyalty Points.");
                stage = 3;
            }
            if (componentId == OPTION_3) {
                sendNPCDialogue(npcId, 9827,
                        "The only way to get Loyalty Points is by playing " + SettingsManager.getSettings().SERVER_NAME + " for at least 30 minutes.");
                stage = 3;
            }
        } else if (stage == 3) {
            end();
        }
    }

    @Override
    public void finish() {

    }

}