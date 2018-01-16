package com.rs.content.cutscenes.actions;

import com.rs.player.Player;
import com.rs.world.ForceTalk;

public class PlayerForceTalkAction extends CutsceneAction {

	private final String text;

	public PlayerForceTalkAction(final String text, final int actionDelay) {
		super(-1, actionDelay);
		this.text = text;
	}

	@Override
	public void process(final Player player, final Object[] cache) {
		player.setNextForceTalk(new ForceTalk(text));
	}

}
