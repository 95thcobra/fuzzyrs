package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.player.points.PlayerPoints;

public class Guessnumber extends Dialogue {

    @Override
    public void start() {
        sendDialogue("This Shop will give you the opportunity to buy items",
                "with your Boss Points. You have got " + player.getPlayerPoints().getPoints(PlayerPoints.BOSS_POINTS)
                        + " Boss Points.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            stage = 0;
            sendOptionsDialogue("Available Options", "Open the Shop",
                    "Don't open the Shop");
        } else if (stage == 0) {
            if (componentId == OPTION_1) {
                player.getInterfaceManager().sendInterface(72);
                player.getPackets().sendGameMessage(
                        "Click on an item to know it's price.");
                player.getPackets().sendIComponentText(72, 31,
                        "Bandos chestplate");
                player.getPackets()
                        .sendIComponentText(72, 32, "Bandos tassets");
                player.getPackets()
                        .sendIComponentText(72, 33, "Armadyl helmet");
                player.getPackets().sendIComponentText(72, 34,
                        "Armadyl chestplate");
                player.getPackets().sendIComponentText(72, 35,
                        "Armadyl plateskirt");
                player.getPackets().sendIComponentText(72, 36, "Bandos hilt");
                player.getPackets().sendIComponentText(72, 37, "Zamorak hilt");
                player.getPackets()
                        .sendIComponentText(72, 38, "Saradomin hilt");
                player.getPackets().sendIComponentText(72, 39, "Armadyl hilt");
                player.getPackets()
                        .sendIComponentText(72, 40, "Close the Shop");
                player.getPackets().sendIComponentText(72, 55,
                        "Boss Points Shop");
                end();
            }
            end();
        }

    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub

    }

}
