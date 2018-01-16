package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.world.WorldTile;

public class PkPortal extends Dialogue {

    @Override
    public void start() {
        stage = 1;
        if (stage == 1) {
            sendOptionsDialogue("Pk Portal", "Safe PvP", "Risk PvP");
            stage = 1;
        }
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == 1) {
            final int option;
            if (componentId == OPTION_1) {
                player.setNextWorldTile(new WorldTile(2815, 5511, 0));
                end();
            }
            if (componentId == OPTION_2) {
                player.setNextWorldTile(new WorldTile(3007, 5511, 0));
                end();
            }
        }
    }

    @Override
    public void finish() {

    }

}