package com.rs.content.cutscenes.actions;

import com.rs.player.Player;
import com.rs.content.cutscenes.Cutscene;

public class DestroyCachedObjectAction extends CutsceneAction {

	public DestroyCachedObjectAction(final int cachedObjectIndex,
			final int actionDelay) {
		super(cachedObjectIndex, actionDelay);
	}

	@Override
	public void process(final Player player, final Object[] cache) {
		final Cutscene scene = (Cutscene) cache[0];
		scene.destroyCache(cache[getCachedObjectIndex()]);
	}

}
