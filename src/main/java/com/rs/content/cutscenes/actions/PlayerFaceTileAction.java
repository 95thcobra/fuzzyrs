package com.rs.content.cutscenes.actions;

import com.rs.world.WorldTile;
import com.rs.player.Player;
import com.rs.content.cutscenes.Cutscene;

public class PlayerFaceTileAction extends CutsceneAction {

	private final int x, y;

	public PlayerFaceTileAction(final int x, final int y, final int actionDelay) {
		super(-1, actionDelay);
		this.x = x;
		this.y = y;
	}

	@Override
	public void process(final Player player, final Object[] cache) {
		final Cutscene scene = (Cutscene) cache[0];
		player.setNextFaceWorldTile(new WorldTile(scene.getBaseX() + x, scene
				.getBaseY() + y, player.getPlane()));
	}

}
