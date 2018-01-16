package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.content.minigames.castlewars.CastleWars;

public class CastleWarsScoreboard extends Dialogue {

    @Override
    public void start() {
        CastleWars.viewScoreBoard(player);

    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        end();

    }

    @Override
    public void finish() {

    }

}
