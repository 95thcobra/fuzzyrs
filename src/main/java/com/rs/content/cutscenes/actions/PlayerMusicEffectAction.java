package com.rs.content.cutscenes.actions;

import com.rs.player.Player;

public class PlayerMusicEffectAction extends CutsceneAction {

	private final int id;

	public PlayerMusicEffectAction(final int id, final int actionDelay) {
		super(-1, actionDelay);
		this.id = id;
	}

	@Override
	public void process(final Player player, final Object[] cache) {
		player.getPackets().sendMusicEffect(id);
	}

}
