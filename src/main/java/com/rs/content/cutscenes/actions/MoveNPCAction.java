package com.rs.content.cutscenes.actions;

import com.rs.player.Player;
import com.rs.content.cutscenes.Cutscene;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

public class MoveNPCAction extends CutsceneAction {

	private final int x, y, plane, movementType;

	public MoveNPCAction(final int cachedObjectIndex, final int x, final int y,
			final boolean run, final int actionDelay) {
		this(cachedObjectIndex, x, y, -1, run ? Player.RUN_MOVE_TYPE
				: Player.WALK_MOVE_TYPE, actionDelay);
	}

	public MoveNPCAction(final int cachedObjectIndex, final int x, final int y,
			final int plane, final int movementType, final int actionDelay) {
		super(cachedObjectIndex, actionDelay);
		this.x = x;
		this.y = y;
		this.plane = plane;
		this.movementType = movementType;
	}

	@Override
	public void process(final Player player, final Object[] cache) {
		final NPC npc = (NPC) cache[getCachedObjectIndex()];
		final Cutscene scene = (Cutscene) cache[0];
		if (movementType == Player.TELE_MOVE_TYPE) {
			npc.setNextWorldTile(new WorldTile(scene.getBaseX() + x, scene
					.getBaseY() + y, plane));
			return;
		}
		npc.setRun(movementType == Player.RUN_MOVE_TYPE);
		npc.addWalkSteps(scene.getBaseX() + x, scene.getBaseY() + y);
	}

}
