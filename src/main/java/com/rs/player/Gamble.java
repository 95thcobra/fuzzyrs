package com.rs.player;

import com.rs.core.utils.Utils;

public class Gamble {

	private static final int CHANCE_OF_WINNING = 70;

	public static void gamble(final Player player, final int cost) {
		if (canAfford(player, cost)) {
			if (calculateWin()) {
				player.getInventory().addItem(995, cost);
				player.getPackets().sendGameMessage(
						"Congratulations, you've won " + cost + " coins!");
				return;
			}
			player.getPackets().sendGameMessage(
					"Bad Luck, you've lost " + cost + " coins.");
			player.getPackets()
					.sendGameMessage(
							"<col=ff000>The chance of win is only 30%!! Just use this when you are really lucky!");
			player.getInventory().deleteItem(995, cost);
			return;
		}
		player.getPackets().sendGameMessage(
				"You can't afford to gamble " + cost + " coins.");
	}

	public static boolean calculateWin() {
		return Utils.random(100) >= CHANCE_OF_WINNING;
	}

	public static boolean canAfford(final Player player, final int amount) {
		return player.getInventory().containsItem(995, amount);
	}

}