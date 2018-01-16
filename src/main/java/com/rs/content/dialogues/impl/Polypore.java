package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;

public class Polypore extends Dialogue {

    public int ID2 = 22494;
    public int Amount2 = 1;

    @Override
    public void start() {
        Item("You have made a Polypore staff. You can use this for permanent.");
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