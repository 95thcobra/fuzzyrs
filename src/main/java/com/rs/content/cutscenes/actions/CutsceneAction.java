package com.rs.content.cutscenes.actions;

import com.rs.player.Player;

public abstract class CutsceneAction {

	private final int actionDelay; // -1 for no delay
	private final int cachedObjectIndex;

	public CutsceneAction(final int cachedObjectIndex, final int actionDelay) {
		this.cachedObjectIndex = cachedObjectIndex;
		this.actionDelay = actionDelay;
	}

	public int getActionDelay() {
		return actionDelay;
	}

	public int getCachedObjectIndex() {
		return cachedObjectIndex;
	}

	public abstract void process(Player player, Object[] cache);

}
