package com.rs.content.actions.skills.agility;

import com.rs.player.Player;
import com.rs.content.actions.skills.Skills;

public class Agility {

	public static boolean hasLevel(final Player player, final int level) {
		if (player.getSkills().getLevel(Skills.AGILITY) < level) {
			player.getPackets().sendGameMessage(
					"You need an agility level of " + level
							+ " to use this obstacle.", true);
			return false;
		}
		return true;
	}

}
