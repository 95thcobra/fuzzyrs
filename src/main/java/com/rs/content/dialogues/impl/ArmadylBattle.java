package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;

public class ArmadylBattle extends Dialogue {

    public int ID2 = 21777;
    public int Amount2 = 1;

    @Override
    public void start() {
        Item("You combine the shards with the battle staff. It's booming with power now!");
        player.getPackets().sendItemOnIComponent(1189, 1, ID2, Amount2);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            switch (componentId) {
                case WHY1:

                    end();
                    break;
            }
        }

    }

    @Override
    public void finish() {

    }
}