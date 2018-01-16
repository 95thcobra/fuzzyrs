package com.rs.content.cutscenes.actions;

import com.rs.player.Player;
import com.rs.content.cutscenes.Cutscene;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

public class NPCFaceTileAction extends CutsceneAction {

	private final int x, y;

	public NPCFaceTileAction(final int cachedObjectIndex, final int x,
			final int y, final int actionDelay) {
		super(cachedObjectIndex, actionDelay);
		this.x = x;
		this.y = y;
	}

	@Override
	public void process(final Player player, final Object[] cache) {
		final Cutscene scene = (Cutscene) cache[0];
		final NPC npc = (NPC) cache[getCachedObjectIndex()];
		npc.setNextFaceWorldTile(new WorldTile(scene.getBaseX() + x, scene
				.getBaseY() + y, npc.getPlane()));
	}

}
