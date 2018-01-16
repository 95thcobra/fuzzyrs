package com.rs.content.dialogues.impl;


import com.rs.content.dialogues.Dialogue;

public class FoodExchange extends Dialogue {

    public int ID1 = 4565;
    public int Amount1 = 1;
    public int ID2 = 10734;
    public int Amount2 = 1;
    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(npcId, 9827,
                "Wooooh! *I give you 5 bread for 5 Wheat* Wooooh!");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Do you got 5 Wheat?", "Yes", "No");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                if (player.getInventory().containsItem(1947, 5)) {
                    player.getInventory().deleteItem(1947, 5);
                    player.getInventory().addItem(2309, 5);
                    end();
                } else {

                    player.sendMessage("You need 5 Wheat for this.");
                    end();
                }
            } else if (componentId == OPTION_2) {
                player.sendMessage("You can find Wheat left of the building.");
                end();
            }
        }
    }

    @Override
    public void finish() {

    }
}