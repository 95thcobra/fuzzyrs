package com.rs.content.cutscenes.actions;

import com.rs.player.Player;

public class PlayerTransformAction extends CutsceneAction {

	private final int npcId;

	public PlayerTransformAction(final int npcId, final int actionDelay) {
		super(-1, actionDelay);
		this.npcId = npcId;
	}

	@Override
	public void process(final Player player, final Object[] cache) {
		player.getAppearance().transformIntoNPC(npcId);
	}

}
