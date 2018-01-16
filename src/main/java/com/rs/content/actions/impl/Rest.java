package com.rs.content.actions.impl;

import com.rs.content.actions.Action;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;

public class Rest extends Action {

	public static int[][] REST_DEFS = {{5713, 1549, 5748},
		{ 11786, 1550, 11788 }, { 5713, 1551, 2921 }

	};

	private int index;

	@Override
	public boolean start(final Player player) {
		if (!process(player))
			return false;
		index = Utils.random(REST_DEFS.length);
		player.setResting(true);
		player.setNextAnimation(new Animation(REST_DEFS[index][0]));
		// player.setNextAnimation(new Animation(REST_DEFS[index][1]));
		player.getAppearance().setRenderEmote(REST_DEFS[index][1]);
		return true;
	}

	@Override
	public boolean process(final Player player) {
		if (player.getPoison().isPoisoned()) {
			player.getPackets().sendGameMessage(
					"You can't rest while you're poisoned.");
			return false;
		}
		if (player.getAttackedByDelay() + 10000 > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage(
					"You can't rest until 10 seconds after the end of combat.");
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(final Player player) {
		return 0;
	}

	@Override
	public void stop(final Player player) {
		player.setResting(false);
		player.setNextAnimation(new Animation(REST_DEFS[index][2]));
		player.getEmotesManager().setNextEmoteEnd();
		player.getAppearance().setRenderEmote(-1);
	}

}