package com.rs.content.dialogues.impl;


import com.rs.content.dialogues.Dialogue;

public class SailorFood extends Dialogue {

    public int ID1 = 4565;
    public int Amount1 = 1;
    public int ID2 = 10734;
    public int Amount2 = 1;
    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(
                npcId,
                9760,
                "Ello matey, we... crashed. But let's make a deal, for every 5 bread I give you 100k.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            sendOptionsDialogue("Sell me 5 bread.", "Here you go",
                    "I have got none");
            stage = 1;
        } else if (stage == 1) {
            if (componentId == OPTION_1) {
                if (player.getInventory().containsItem(2309, 5)) {
                    player.getInventory().deleteItem(2309, 5);
                    player.getInventory().addItem(995, 100000);
                    end();
                } else {

                    player.sendMessage("Maybe you should try to find a local farmer.");
                    end();
                }
            } else if (componentId == OPTION_2) {
                player.sendMessage("Maybe you should try to find a local farmer.");
                end();
            }
        }
    }

    @Override
    public void finish() {

    }
}