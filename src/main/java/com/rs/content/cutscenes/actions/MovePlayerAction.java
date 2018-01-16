package com.rs.content.cutscenes.actions;

import com.rs.player.Player;
import com.rs.world.WorldTile;
import com.rs.content.cutscenes.Cutscene;

public class MovePlayerAction extends CutsceneAction {

	private final int x, y, plane, movementType;

	public MovePlayerAction(final int x, final int y, final boolean run,
			final int actionDelay) {
		this(x, y, -1, run ? Player.RUN_MOVE_TYPE : Player.WALK_MOVE_TYPE,
				actionDelay);
	}

	public MovePlayerAction(final int x, final int y, final int plane,
			final int movementType, final int actionDelay) {
		super(-1, actionDelay);
		this.x = x;
		this.y = y;
		this.plane = plane;
		this.movementType = movementType;
	}

	@Override
	public void process(final Player player, final Object[] cache) {
		final Cutscene scene = (Cutscene) cache[0];
		if (movementType == Player.TELE_MOVE_TYPE) {
			player.setNextWorldTile(new WorldTile(scene.getBaseX() + x, scene
					.getBaseY() + y, plane));
			return;
		}
		player.setRun(movementType == Player.RUN_MOVE_TYPE);
		player.addWalkSteps(scene.getBaseX() + x, scene.getBaseY() + y);
	}

}
