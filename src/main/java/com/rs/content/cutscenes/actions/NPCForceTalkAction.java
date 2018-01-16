package com.rs.content.cutscenes.actions;

import com.rs.player.Player;
import com.rs.world.ForceTalk;
import com.rs.world.npc.NPC;

public class NPCForceTalkAction extends CutsceneAction {

	private final String text;

	public NPCForceTalkAction(final int cachedObjectIndex, final String text,
			final int actionDelay) {
		super(cachedObjectIndex, actionDelay);
		this.text = text;
	}

	@Override
	public void process(final Player player, final Object[] cache) {
		final NPC npc = (NPC) cache[getCachedObjectIndex()];
		npc.setNextForceTalk(new ForceTalk(text));
	}

}
