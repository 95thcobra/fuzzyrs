package com.rs.content.cutscenes.actions;

import com.rs.player.Player;
import com.rs.world.Graphics;
import com.rs.world.npc.NPC;

public class NPCGraphicAction extends CutsceneAction {

	private final Graphics gfx;

	public NPCGraphicAction(final int cachedObjectIndex, final Graphics gfx,
			final int actionDelay) {
		super(cachedObjectIndex, actionDelay);
		this.gfx = gfx;
	}

	@Override
	public void process(final Player player, final Object[] cache) {
		final NPC npc = (NPC) cache[getCachedObjectIndex()];
		npc.setNextGraphics(gfx);
	}

}
