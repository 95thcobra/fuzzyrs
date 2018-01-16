package com.rs.content.cutscenes.actions;

import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.npc.NPC;

public class NPCAnimationAction extends CutsceneAction {

	private final Animation anim;

	public NPCAnimationAction(final int cachedObjectIndex,
			final Animation anim, final int actionDelay) {
		super(cachedObjectIndex, actionDelay);
		this.anim = anim;
	}

	@Override
	public void process(final Player player, final Object[] cache) {
		final NPC npc = (NPC) cache[getCachedObjectIndex()];
		npc.setNextAnimation(anim);
	}

}
