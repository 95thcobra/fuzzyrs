package com.rs.content.actions.skills.crafting;

import com.rs.content.actions.Action;
import com.rs.content.actions.skills.Skills;
import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.item.Item;

/**
 * @author Kova+ (Alex)
 *
 */
public class JewerlyCrafting extends Action {

	private static int[] GEMS = { 0, 1607, 1605, 1603, 1601, 1615, 6573 };
	private static int[] RINGS = { 1635, 2550, 2552, 2568, 2570, 2572, 6583 };
	private static int[] BRACELETS = { 11069, 11074, 11079, 11088, 11095,
			11118, 11133 };
	private static int[] NECKLACES = { 1654, 3853, 5521, 11194, 11090, 11105,
			11128 };
	private static int[] AMULETS = { 1673, 1675, 1677, 1679, 1681, 1683, 6579 };
	private static int[] RING_COMPONENTS = { 81, 83, 85, 87, 89, 91, 93, 96 };
	private static int[] NECKLACE_COMPONENTS = { 67, 69, 71, 73, 75, 77, 79 };
	private static int[] AMULET_COMPONENTS = { 52, 54, 56, 58, 60, 62, 64 };
	private static int[] BRACELET_COMPONENTS = { 32, 34, 36, 38, 40, 42, 44 };
	private static int[] MOULDS = { 1592, 1595, 1597, 11065 };
	private static int NO_RING = 1647, NO_NECKLACE = 1666, NO_AMULET = 1685,
			NO_BRACELET = 11067, GOLD_BAR = 2357;
	private final Item gold, gem;
	private final JewerlyData data;
	private int amount;

	public JewerlyCrafting(final JewerlyData data, final Item gold,
						   final Item gem, final int amount) {
		this.data = data;
		this.gold = gold;
		this.gem = gem;
		this.amount = amount;
	}

	public static void sendInterface(final Player player) {
		player.getInterfaceManager().sendInterface(446);
		if (player.getInventory().containsItem(MOULDS[0], 1)) {
			for (final int element : RING_COMPONENTS) {
				player.getPackets().sendItemOnIComponent(446, element, NO_RING,
						1);
				player.getPackets().sendIComponentText(446, 98, "");
			}
			for (int i = 0; i < GEMS.length; i++) {
				if (player.getInventory().containsItem(GOLD_BAR, 1)
						&& player.getInventory().containsItem(GEMS[i], 1)) {
					player.getPackets().sendItemOnIComponent(446,
							RING_COMPONENTS[i], RINGS[i], 1);
				}
			}
			if (player.getInventory().containsItem(MOULDS[0], 1)
					&& player.getInventory().containsItem(GOLD_BAR, 1)) {
				player.getPackets().sendItemOnIComponent(446,
						RING_COMPONENTS[0], RINGS[0], 1);
			}
		}
		if (player.getInventory().containsItem(MOULDS[1], 1)) {
			for (final int element : NECKLACE_COMPONENTS) {
				player.getPackets().sendItemOnIComponent(446, element,
						NO_NECKLACE, 1);
				player.getPackets().sendIComponentText(446, 22, "");
			}
			for (int i = 0; i < GEMS.length; i++) {
				if (player.getInventory().containsItem(GOLD_BAR, 1)
						&& player.getInventory().containsItem(GEMS[i], 1)) {
					player.getPackets().sendItemOnIComponent(446,
							NECKLACE_COMPONENTS[i], NECKLACES[i], 1);
				}
			}
			if (player.getInventory().containsItem(MOULDS[1], 1)
					&& player.getInventory().containsItem(GOLD_BAR, 1)) {
				player.getPackets().sendItemOnIComponent(446,
						NECKLACE_COMPONENTS[0], NECKLACES[0], 1);
			}
		}
		if (player.getInventory().containsItem(MOULDS[2], 1)) {
			for (final int element : AMULET_COMPONENTS) {
				player.getPackets().sendItemOnIComponent(446, element,
						NO_AMULET, 1);
				player.getPackets().sendIComponentText(446, 66, "");
			}
			for (int i = 0; i < GEMS.length; i++) {
				if (player.getInventory().containsItem(GOLD_BAR, 1)
						&& player.getInventory().containsItem(GEMS[i], 1)) {
					player.getPackets().sendItemOnIComponent(446,
							AMULET_COMPONENTS[i], AMULETS[i], 1);
				}
			}
			if (player.getInventory().containsItem(MOULDS[2], 1)
					&& player.getInventory().containsItem(GOLD_BAR, 1)) {
				player.getPackets().sendItemOnIComponent(446,
						AMULET_COMPONENTS[0], AMULETS[0], 1);
			}
		}
		if (player.getInventory().containsItem(MOULDS[3], 1)) {
			for (final int element : BRACELET_COMPONENTS) {
				player.getPackets().sendItemOnIComponent(446, element,
						NO_BRACELET, 1);
				player.getPackets().sendIComponentText(446, 51, "");
			}
			for (int i = 0; i < GEMS.length; i++) {
				if (player.getInventory().containsItem(GOLD_BAR, 1)
						&& player.getInventory().containsItem(GEMS[i], 1)) {
					player.getPackets().sendItemOnIComponent(446,
							BRACELET_COMPONENTS[i], BRACELETS[i], 1);
				}
			}
			if (player.getInventory().containsItem(MOULDS[3], 1)
					&& player.getInventory().containsItem(GOLD_BAR, 1)) {
				player.getPackets().sendItemOnIComponent(446,
						BRACELET_COMPONENTS[0], BRACELETS[0], 1);
			}
		}
	}

	@Override
	public boolean start(final Player player) {
		if (checkAll(player)) {
			setActionDelay(player, 1);
			return true;
		}
		return false;
	}

	public boolean checkAll(final Player player) {
		if (player.getSkills().getLevel(Skills.CRAFTING) < data.getLevel()) {
			player.getDialogueManager().startDialogue(
					SimpleMessage.class,
					"You need a crafting level of " + data.getLevel()
							+ " to create that.");
			return false;
		}
		if (!player.getInventory().containsItem(gold.getId(), 1)) {
			player.getDialogueManager().startDialogue(
					SimpleMessage.class,
					"You don't have any more "
							+ ItemDefinitions.getItemDefinitions(gold.getId())
									.getName().toLowerCase() + "s to use.");
			return false;
		}
		if (!player.getInventory().containsItem(gem.getId(), 1)) {
			if (gem.getId() == -1)
				return true;
			player.getDialogueManager().startDialogue(
					SimpleMessage.class,
					"You don't have any more "
							+ ItemDefinitions.getItemDefinitions(gem.getId())
									.getName().toLowerCase() + "s to use.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(final Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(final Player player) {
		if (gem.getId() == -1) {
			player.getInventory().deleteItem(gold.getId(), 1);
			player.getInventory().addItem(data.getOutcome(), 1);
			player.getSkills().addXp(Skills.CRAFTING, data.getXp());
			player.getPackets().sendGameMessage(
					"You shape the gold bar with the mould to make  "
							+ ItemDefinitions
									.getItemDefinitions(data.getOutcome())
									.getName().toLowerCase() + ".", true);
		} else {
			player.getInventory().deleteItem(gold.getId(), 1);
			player.getInventory().deleteItem(gem.getId(), 1);
			player.getInventory().addItem(data.getOutcome(), 1);
			player.getSkills().addXp(Skills.CRAFTING, data.getXp());
			player.getPackets().sendGameMessage(
					"You bind the Gold bar and the "
							+ ItemDefinitions.getItemDefinitions(gem.getId())
									.getName().toLowerCase()
							+ " together to make a "
							+ ItemDefinitions
									.getItemDefinitions(data.getOutcome())
									.getName().toLowerCase() + ".", true);
		}
		amount--;
		if (amount <= 0)
			return -1;
		player.setNextAnimation(new Animation(3243));
		return 0;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}

	public enum JewerlyData {
		GOLD_RING(5, 15, 1635), SAPPHIRE_RING(20, 40, 2550), EMERALD_RING(27,
				55, 2552), RUBY_RING(34, 70, 2568), DIAMOND_RING(43, 85, 2570), DRAGONSTONE_RING(
				55, 100, 2572), ONYX_RING(67, 115, 6583),

		GOLD_NECKLACE(6, 20, 1654), SAPPHIRE_NECKLACE(22, 55, 3853), EMERALD_NECKLACE(
				29, 60, 5521), RUBY_NECKLACE(40, 75, 11194), DIAMOND_NECKLACE(
				56, 90, 11090), DRAGONSTONE_NECKLACE(72, 105, 11105), ONYX_NECKLACE(
				82, 120, 11128),

		GOLD_BRACELET(7, 25, 11069), SAPPHIRE_BRACELET(23, 60, 11074), EMERALD_BRACELET(
				30, 65, 11079), RUBY_BRACELET(42, 80, 11088), DIAMOND_BRACELET(
				58, 95, 11095), DRAGONSTONE_BRACELET(74, 110, 11118), ONYX_BRACELET(
				84, 125, 11133),

		GOLD_AMULET(8, 30, 1673), SAPPHIRE_AMULET(24, 65, 1675), EMERALD_AMULET(
				31, 70, 1677), RUBY_AMULET(50, 85, 1679), DIAMOND_AMULET(70,
				100, 1681), DRAGONSTONE_AMULET(80, 150, 1683), ONYX_AMULET(90,
				165, 6579);

		private int level, outcome;
		private double xp;

		JewerlyData(final int levelNeeded, final double xp,
					final int outcome) {
			this.level = levelNeeded;
			this.xp = xp;
			this.outcome = outcome;
		}

		private int getLevel() {
			return level;
		}

		private int getOutcome() {
			return outcome;
		}

		private double getXp() {
			return xp;
		}
	}
}