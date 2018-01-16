package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.minigames.ZarosGodwars;
import com.rs.player.controlers.ZGDController;
import com.rs.world.WorldTile;

public final class NexEntrance extends Dialogue {

    @Override
    public void start() {
        sendDialogue("The room beyond this point is a prison!",
                "There is no way out other than death or teleport.",
                "Only those who endure dangerous encounters should proceed.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            stage = 0;
            sendOptionsDialogue(
                    "There are currently " + ZarosGodwars.getPlayersCount()
                            + " people fighting.<br>Do you wish to join them?",
                    "Climb down.", "Stay here.");
        } else if (stage == 0) {
            if (componentId == OPTION_1) {
                player.setNextWorldTile(new WorldTile(2911, 5204, 0));
                player.getControllerManager().startController(ZGDController.class);
            }
            end();
        }

    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub

    }

}
