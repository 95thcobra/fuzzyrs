package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.player.controlers.FightKiln;

public class FightKilnDialogue extends Dialogue {

    @Override
    public void start() {
        player.lock();
        sendDialogue("You journey directly to the Kiln.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        end();
    }

    @Override
    public void finish() {
        player.getControllerManager().startController(FightKiln.class, 0);
    }

}
