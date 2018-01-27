package com.rs.content.minigames;

import com.rs.utils.Utils;
import com.rs.player.Player;

/**
 * Adds a statuette on kill.
 * 
 * @author BongoProd Rune-Server, Bad way to do it Xd
 */

public class StatuetteLoot {

	public static final int[] KEY_HALVES = { 985, 987, 2677 };
	public static final int KEY = 2404;
	public static final int Animation = 5078;
	private static final int[] CHEST_REWARDS = {14892, 14891, 14890, 14889,
			14888, 14887, 14886, 14885, 14884, 14883, 14882, 14881, 14880,
			14879, 14878, 14877, 14876};

	/**
	 * Represents the key being made. Using tooth halves.
	 */
	public static void makeKey(final Player p) {
		if (p.getInventory().containsItem(toothHalf(), 1)
				&& p.getInventory().containsItem(loopHalf(), 1)) {
			p.getInventory().deleteItem(toothHalf(), 1);
			p.getInventory().deleteItem(loopHalf(), 1);
			p.getInventory().addItem(KEY, 1);
			p.sendMessage("You made a crystal key.");
		}
	}

	/**
	 * If the player can open the chest.
	 */
	public static boolean canOpen(final Player p) {
		return p.isYesYes == 0;
	}

	/**
	 * When the player searches the chest.
	 */
	public static void searchChest(final Player p) {
		if (canOpen(p)) {
			p.getInventory().addItem(
					CHEST_REWARDS[Utils.random(getLength() - 1)], 1);
		}
	}

	public static int getLength() {
		return CHEST_REWARDS.length;
	}

	/**
	 * Represents the toothHalf of the key.
	 */
	public static int toothHalf() {
		return KEY_HALVES[0];
	}

	/**
	 * Represent the loop half of the key.
	 */
	public static int loopHalf() {
		return KEY_HALVES[1];
	}

}