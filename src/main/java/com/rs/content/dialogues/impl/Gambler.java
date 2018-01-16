package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.player.Gamble;

public class Gambler extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(npcId, 9827, "Hello, how much would you like to Gamble? <col=ff000>THE WIN RATE IS ONLY 30% RIGHT NOW! JUST DO THIS WHEN YOU FEEL LUCKY!");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("How much do you wish to gamble?",
                    "10000gp (10k)", "100000gp (100k)", "1000000gp (1m)",
                    "10000000gp (10m)", "I don't want to gamble.");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                Gamble.gamble(player, 10000);
                end();
            } else if (componentId == OPTION_2) {
                Gamble.gamble(player, 100000);
                end();
            } else if (componentId == OPTION_3) {
                Gamble.gamble(player, 1000000);
                end();
            } else if (componentId == OPTION_4) {
                Gamble.gamble(player, 10000000);
                end();
            } else if (componentId == OPTION_5) {
                player.getPackets().sendGameMessage("Pass by next time!");
                end();
            }
        }
    }

    @Override
    public void finish() {

    }
}