package com.rs.player;

import com.rs.content.cutscenes.Cutscene;
import com.rs.content.cutscenes.CutsceneHandler;

public final class CutscenesManager {

	private final Player player;
	private Cutscene cutscene;

	/*
	 * cutscene play stuff
	 */

	public CutscenesManager(final Player player) {
		this.player = player;
	}

	public void process() {
		if (cutscene == null)
			return;
		if (cutscene.process(player))
			return;
		cutscene = null;
	}

	public void logout() {
		if (hasCutscene()) {
			cutscene.logout(player);
		}
	}

	public boolean hasCutscene() {
		return cutscene != null;
	}

	public boolean play(final Object key) {
		if (hasCutscene())
			return false;
		final Cutscene cutscene = (Cutscene) (key instanceof Cutscene ? key
				: CutsceneHandler.getCutscene(key));
		if (cutscene == null)
			return false;
		cutscene.createCache(player);
		this.cutscene = cutscene;
		return true;
	}

}
