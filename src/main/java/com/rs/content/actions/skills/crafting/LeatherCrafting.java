package com.rs.content.actions.skills.crafting;

import com.rs.Server;
import com.rs.content.actions.Action;
import com.rs.content.actions.skills.Skills;
import com.rs.content.dialogues.impl.LeatherCraftingD;
import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.core.settings.SettingsManager;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.item.Item;

import java.util.HashMap;
import java.util.Map;

/**************************
 * \ \/\/\/\/\/\/\/\/\/\/\/\/*
 *
 * @author "Raghav/Own4g3" * \/\/\/\/\/\/\/\/\/\/\/\/* \
 **************************/

public class LeatherCrafting extends Action {

	public static final Item NEEDLE = new Item(1733);
	public static final Item THREAD = new Item(1734);
	public static final int LEATHER[] = { 1745, 2505, 2507, 2509, 24374 };
	public static final int PRODUCTS[][] = { { 1065, 1099, 1135 },
			{ 2487, 2493, 2499 }, { 2489, 2495, 2501 }, { 2491, 2497, 2503 },
			{ 24376, 24379, 24382 } };
	public final Animation CRAFT_ANIMATION = new Animation(1249);
	private final LeatherData data;
	private int quantity;
	private int removeThread = 5;

	public LeatherCrafting(final LeatherData data, final int quantity) {
		this.data = data;
		this.quantity = quantity;
	}

	public static boolean handleItemOnItem(final Player player,
			final Item itemUsed, final Item usedWith) {
		for (final int element : LEATHER) {
			if (itemUsed.getId() == element || usedWith.getId() == element) {
				player.getTemporaryAttributtes().put("leatherType", element);
				final int index = getIndex(player);
				if (index == -1)
					return true;
				player.getDialogueManager().startDialogue(LeatherCraftingD.class,
						LeatherData.forId(PRODUCTS[index][0]),
						LeatherData.forId(PRODUCTS[index][1]),
						LeatherData.forId(PRODUCTS[index][2]));
				return true;
			}
		}
		return false;
	}

	public static int getIndex(final Player player) {
		final int leather = (Integer) player.getTemporaryAttributtes().get(
				"leatherType");
		if (leather == LEATHER[0])
			return 0;
		if (leather == LEATHER[1])
			return 1;
		if (leather == LEATHER[2])
			return 2;
		if (leather == LEATHER[3])
			return 3;
		if (leather == LEATHER[4])
			return 4;
		return -1;
	}

	@Override
	public boolean start(final Player player) {
		if (!checkAll(player))
			return false;
		setActionDelay(player, 1);
		player.setNextAnimation(CRAFT_ANIMATION);
		return true;
	}

	private boolean checkAll(final Player player) {
		if (data.getRequiredLevel() > player.getSkills().getLevel(
				Skills.CRAFTING)) {
			player.getDialogueManager().startDialogue(
					SimpleMessage.class,
					"You need a crafting level of " + data.getRequiredLevel()
							+ " to craft this hide.");
			return false;
		}
		if (player.getInventory().getItems().getNumberOf(data.getLeatherId()) < data
				.getLeatherAmount()) {
			player.getPackets().sendGameMessage(
					"You don't have enough amount of hides in your inventory.");
			return false;
		}
		if (!player.getInventory().getItems().containsOne(THREAD)) {
			player.getPackets()
					.sendGameMessage("You need a thread to do this.");
			return false;
		}
		if (!player.getInventory().getItems().containsOne(NEEDLE)) {
			player.getPackets().sendGameMessage(
					"You need a needle to craft leathers.");
			return false;
		}
		if (!player.getInventory().containsOneItem(data.getLeatherId())) {
			player.getPackets().sendGameMessage(
					"You've ran out of "
							+ ItemDefinitions
									.getItemDefinitions(data.getLeatherId())
									.getName().toLowerCase() + ".");
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
		player.getInventory().deleteItem(data.getLeatherId(),
				data.getLeatherAmount());
		player.getInventory().addItem(data.getFinalProduct(), 1);
		player.getSkills().addXp(Skills.CRAFTING,
				data.getExperience() * Server.getInstance().getSettingsManager().getSettings().getSkillingXpRate());
		player.getPackets().sendGameMessage(
				"You make a " + data.getName().toLowerCase() + ".");
		quantity--;
		removeThread--;
		if (removeThread == 0) {
			removeThread = 5;
			player.getInventory().removeItems(THREAD); // every 5 times, your
			// thread get deleted.
			player.getPackets().sendGameMessage(
					"You use up a reel of your thread.");
		}
		if (Utils.getRandom(30) <= 3) {
			player.getInventory().removeItems(NEEDLE);
			player.getPackets().sendGameMessage("Your needle has broken.");
		}
		if (quantity <= 0)
			return -1;
		player.setNextAnimation(CRAFT_ANIMATION);
		return 0;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}

	public enum LeatherData {
		GREEN_D_HIDE_VAMBS(1745, 1, 1065, 57, 62),

		GREEN_D_HIDE_CHAPS(1745, 2, 1099, 60, 124),

		GREEN_D_HIDE_BODY(1745, 3, 1135, 63, 186),

		BLUE_D_HIDE_VAMBS(2505, 1, 2487, 66, 70),

		BLUE_D_HIDE_CHAPS(2505, 2, 2493, 68, 140),

		BLUE_D_HIDE_BODY(2505, 3, 2499, 71, 210),

		RED_D_HIDE_VAMBS(2507, 1, 2489, 73, 78),

		RED_D_HIDE_CHAPS(2507, 2, 2495, 75, 156),

		RED_D_HIDE_BODY(2507, 3, 2501, 77, 234),

		BLACK_D_HIDE_VAMBS(2509, 1, 2491, 79, 86),

		BLACK_D_HIDE_CHAPS(2509, 2, 2497, 82, 172),

		BLACK_D_HIDE_BODY(2509, 3, 2503, 84, 258),

		ROYAL_D_HIDE_VAMBS(24374, 1, 24376, 87, 94),

		ROYAL_D_HIDE_CHAPS(24374, 2, 24379, 89, 188),

		ROYAL_D_HIDE_BODY(24374, 3, 24382, 93, 282);

		private static Map<Integer, LeatherData> leatherItems = new HashMap<Integer, LeatherData>();

		static {
			for (final LeatherData leather : LeatherData.values()) {
				leatherItems.put(leather.finalProduct, leather);
			}
		}

		private int leatherId, leatherAmount, finalProduct, requiredLevel;
		private double experience;
		private String name;

		LeatherData(final int leatherId, final int leatherAmount,
					final int finalProduct, final int requiredLevel,
					final double experience) {
			this.leatherId = leatherId;
			this.leatherAmount = leatherAmount;
			this.finalProduct = finalProduct;
			this.requiredLevel = requiredLevel;
			this.experience = experience;
			this.name = ItemDefinitions.getItemDefinitions(getFinalProduct())
					.getName().replace("d'hide", "");
		}

		public static LeatherData forId(final int id) {
			return leatherItems.get(id);
		}

		public int getLeatherId() {
			return leatherId;
		}

		public int getLeatherAmount() {
			return leatherAmount;
		}

		public int getFinalProduct() {
			return finalProduct;
		}

		public int getRequiredLevel() {
			return requiredLevel;
		}

		public double getExperience() {
			return experience;
		}

		public String getName() {
			return name;
		}
	}

}