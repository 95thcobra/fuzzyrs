package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.player.content.PartyRoom;

public class PartyRoomLever extends Dialogue {

    @Override
    public void start() {
        sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE,
                "Balloon Bonanza (1000 coins).", "Nightly Dance (500 coins).",
                "No action.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (componentId == 2) {
            PartyRoom.purchase(player, true);
        } else if (componentId == 3) {
            PartyRoom.purchase(player, false);
        }
        end();
    }

    @Override
    public void finish() {

    }
}
