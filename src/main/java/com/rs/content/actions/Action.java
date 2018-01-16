package com.rs.content.actions;

import com.rs.player.Player;

public abstract class Action {

	public abstract boolean start(Player player);

	public abstract boolean process(Player player);

	public abstract int processWithDelay(Player player);

	public abstract void stop(Player player);

	protected final void setActionDelay(final Player player, final int delay) {
		player.getActionManager().setActionDelay(delay);
	}
}
