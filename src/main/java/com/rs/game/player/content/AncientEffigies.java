package com.rs.game.player.content;

import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
/**
 * @author Raghav/Own4g3 <Raghav_ftw@hotmail.com>
 * 
 * @author Gircat <gircat101@gmail.com> 
 * Modified on Jul 29, 2014 at 10:58:28 PM.
 * 
 */
public class AncientEffigies {

	public static int[] SKILL_1 = { Skills.AGILITY, Skills.CONSTRUCTION,
			Skills.COOKING, Skills.FISHING, Skills.FLETCHING, Skills.HERBLORE,
			Skills.MINING, Skills.SUMMONING };

	public static int[] SKILL_2 = { Skills.CRAFTING, Skills.THIEVING,
			Skills.FIREMAKING, Skills.FARMING, Skills.WOODCUTTING,
			Skills.HUNTER, Skills.SMITHING, Skills.RUNECRAFTING };

	public static final int STARVED_ANCIENT_EFFIGY = 18778,
			NOURISHED_ANCIENT_EFFIGY = 18779, SATED_ANCIENT_EFFIGY = 18780,
			GORGED_ANCIENT_EFFIGY = 18781, DRAGONKIN_LAMP = 18782;

	public static int getRequiredLevel(int id) {
		switch (id) {
		case STARVED_ANCIENT_EFFIGY:
			return 91;
		case NOURISHED_ANCIENT_EFFIGY:
			return 93;
		case SATED_ANCIENT_EFFIGY:
			return 95;
		case GORGED_ANCIENT_EFFIGY:
			return 97;
		}
		return -1;
	}

	public static String getMessage(int skill) {
		switch (skill) {
		case Skills.AGILITY:
			return "deftness and precision";
		case Skills.CONSTRUCTION:
			return "buildings and security";
		case Skills.COOKING:
			return "fire and preparation";
		case Skills.FISHING:
			return "life and cultivation";
		case Skills.FLETCHING:
			return "lumber and woodworking";
		case Skills.HERBLORE:
			return "flora and fuana";
		case Skills.MINING:
			return "metalwork and minerals";
		case Skills.SUMMONING:
			return "binding essence and spirits";
		}
		return null;
	}

	public static int getExp(int itemId) {
		switch (itemId) {
		case STARVED_ANCIENT_EFFIGY:
			return 15000;
		case NOURISHED_ANCIENT_EFFIGY:
			return 20000;
		case SATED_ANCIENT_EFFIGY:
			return 25000;
		case GORGED_ANCIENT_EFFIGY:
			return 30000;
		}
		return -1;
	}

	public static void effigyInvestigation(Player player, int id) {
		Inventory inv = player.getInventory();
		inv.deleteItem(id, 1);
		if (inv.containsOneItem(STARVED_ANCIENT_EFFIGY))
			inv.addItem(NOURISHED_ANCIENT_EFFIGY, 1);
		else if (inv.containsOneItem(NOURISHED_ANCIENT_EFFIGY))
			inv.addItem(SATED_ANCIENT_EFFIGY, 1);
		else if (inv.containsOneItem(SATED_ANCIENT_EFFIGY))
			inv.addItem(GORGED_ANCIENT_EFFIGY, 1);
		else if (inv.containsOneItem(GORGED_ANCIENT_EFFIGY))
			inv.addItem(DRAGONKIN_LAMP, 1);
	}
}
