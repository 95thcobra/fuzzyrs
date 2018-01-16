package com.rs.content.cutscenes.actions;

import com.rs.player.Player;
import com.rs.world.Graphics;

public class PlayerGraphicAction extends CutsceneAction {

	private final Graphics gfx;

	public PlayerGraphicAction(final Graphics gfx, final int actionDelay) {
		super(-1, actionDelay);
		this.gfx = gfx;
	}

	@Override
	public void process(final Player player, final Object[] cache) {
		player.setNextGraphics(gfx);
	}

}
