package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.world.World;

public class EmptyCashBag extends Dialogue {

    @Override
    public void start() {
        player.getInterfaceManager().sendChatBoxInterface(1189);
        player.getInventory().deleteItem(10831, 1);
        player.getPackets().sendItemOnIComponent(1189, 1, 10831, 1);
        player.getPackets().sendIComponentText(1189, 4,
                "You have been trolled!");
        World.sendWorldMessage(
                "<img=7><col=ff0000>News: " + player.getDisplayName()
                        + " has just been trolled with an empty cash bag!",
                false);
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (interfaceId == 1189 && componentId == 9) {
        }
        end();
    }

    @Override
    public void finish() {

    }

}