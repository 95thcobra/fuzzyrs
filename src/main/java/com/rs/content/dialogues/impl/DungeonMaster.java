package com.rs.content.dialogues.impl;


import com.rs.content.dialogues.Dialogue;

public class DungeonMaster extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(npcId, 9827,
                "Hello, how much Dungeoneering Experience you want to exchange?");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue(
                    "How much Dungeoneering EXP you want to exchange?",
                    "10000gp (10k)", "100k Rusty Coins (100K XP)",
                    "1m Rusty Coins (100K XP)", "10m Rusty Coins (10m XP)",
                    "I don't want to exchange.");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                if (player.getInventory().containsItem(18201, 10000)) {
                    player.getInventory().deleteItem(18201, 10000);
                    player.getSkills().addXp(24, 8000);
                } else {
                    player.sendMessage("You can't afford to 10.000 rusty coins.");
                }
                end();
            } else if (componentId == OPTION_2) {
                if (player.getInventory().containsItem(18201, 100000)) {
                    player.getInventory().deleteItem(18201, 100000);
                    player.getSkills().addXp(24, 80000);
                } else {
                    player.sendMessage("You can't afford to 100.000 rusty coins.");
                }
                end();
            } else if (componentId == OPTION_3) {
                if (player.getInventory().containsItem(18201, 1000000)) {
                    player.getInventory().deleteItem(18201, 8000000);
                    player.getSkills().addXp(24, 420000);
                } else {
                    player.sendMessage("You can't afford to 1.000.000 rusty coins.");
                }
                end();
            } else if (componentId == OPTION_4) {
                if (player.getInventory().containsItem(18201, 10000000)) {
                    player.getInventory().deleteItem(18201, 10000000);
                    player.getSkills().addXp(24, 200000000);
                } else {
                    player.sendMessage("You can't afford to 10.000.000 rusty coins.");
                }
                end();
            } else if (componentId == OPTION_5) {
                player.sendMessage("Please come back later.");
                end();
            }
        }
    }

    @Override
    public void finish() {

    }
}