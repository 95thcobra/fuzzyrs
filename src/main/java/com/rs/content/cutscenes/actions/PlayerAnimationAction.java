package com.rs.content.cutscenes.actions;

import com.rs.player.Player;
import com.rs.world.Animation;

public class PlayerAnimationAction extends CutsceneAction {

	private final Animation anim;

	public PlayerAnimationAction(final Animation anim, final int actionDelay) {
		super(-1, actionDelay);
		this.anim = anim;
	}

	@Override
	public void process(final Player player, final Object[] cache) {
		player.setNextAnimation(anim);
	}

}
