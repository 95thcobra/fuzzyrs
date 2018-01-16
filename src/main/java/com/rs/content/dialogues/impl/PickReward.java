package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;

public class PickReward extends Dialogue {

    public int ID1 = 4565;
    public int Amount1 = 1;
    public int ID2 = 10734;
    public int Amount2 = 1;

    @Override
    public void start() {
        Options2("Rewards", "Item1", "Item2");
        player.getPackets().sendItemOnIComponent(4565, 4, ID1, Amount1);
        player.getPackets().sendItemOnIComponent(10734, 5, ID2, Amount2);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            switch (componentId) {
                case OPTION1:
                    player.getInventory().addItem(ID1, Amount1);
                    end();
                    break;
                case OPTION2:
                    player.getInventory().addItem(ID2, Amount2);
                    end();
                    break;
            }
        }
    }

    @Override
    public void finish() {

    }
}
