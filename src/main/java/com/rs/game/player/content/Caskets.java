package com.rs.game.player.content;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class Caskets {

	private static final int[] ITEMS = { 1712, 7323, 1073, 2912, 1071, 12523, };

	public static final boolean lootCasket(Player player, Item item) {
		if (player.isDead())
			return false;
		if (item.getName().equals("Casket")) {
			player.getInventory().deleteItem(item.getId(), 1);
			player.addStopDelay(1);
			switch (Utils.getRandom(1)) {
			case 0:
				if (!player.isDonator())
					player.getInventory().addItem(995, Utils.random(675, 1174));
				else
					player.getInventory().addItem(995, Utils.random(7965, 10871));
				return true;
			case 1:
				player.getInventory().addItem(ITEMS[Utils.getRandom(ITEMS.length - 1)], 1);
				player.getInventory().addItem(7937, Utils.random(197, 364));
				return true;
			}
		}
		return false;
	}

}