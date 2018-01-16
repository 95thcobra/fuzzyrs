package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;

public class DTSpectateReq extends Dialogue {

    @Override
    public void start() {
        sendDialogue(

                "You don't have the requirements to play this content, but you can",
                "spectate some of the matches taking place if you would like.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        player.getDominionTower().openSpectate();
        end();
    }

    @Override
    public void finish() {

    }

}
