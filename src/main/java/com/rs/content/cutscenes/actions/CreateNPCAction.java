package com.rs.content.cutscenes.actions;

import com.rs.player.Player;
import com.rs.content.cutscenes.Cutscene;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

public class CreateNPCAction extends CutsceneAction {

	private final int id, x, y, plane;

	public CreateNPCAction(final int cachedObjectIndex, final int id,
			final int x, final int y, final int plane, final int actionDelay) {
		super(cachedObjectIndex, actionDelay);
		this.id = id;
		this.x = x;
		this.y = y;
		this.plane = plane;
	}

	@Override
	public void process(final Player player, final Object[] cache) {
		final Cutscene scene = (Cutscene) cache[0];
		if (cache[getCachedObjectIndex()] != null) {
			scene.destroyCache(cache[getCachedObjectIndex()]);
		}
		final NPC npc = (NPC) (cache[getCachedObjectIndex()] = World.spawnNPC(
				id, new WorldTile(scene.getBaseX() + x, scene.getBaseY() + y,
						plane), -1, true, true));
		npc.setRandomWalk(false);
	}

}
