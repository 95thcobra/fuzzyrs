package com.rs.content.actions.skills.crafting;

import com.rs.content.actions.Action;
import com.rs.content.actions.skills.Skills;
import com.rs.content.dialogues.impl.GemCuttingD;
import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.item.Item;

public class GemCutting extends Action {

	private final Gem gem;
	private int quantity;

	public GemCutting(final Gem gem, final int quantity) {
		this.gem = gem;
		this.quantity = quantity;
	}

	public static void cut(final Player player, final Gem gem) {
		if (player.getInventory().getItems()
				.getNumberOf(new Item(gem.getUncut(), 1)) <= 1) {
			// 1 lets start
			player.getActionManager().setAction(new GemCutting(gem, 1));
		} else {
			player.getDialogueManager().startDialogue(GemCuttingD.class, gem);
		}
	}

	public boolean checkAll(final Player player) {
		if (player.getSkills().getLevel(Skills.CRAFTING) < gem
				.getLevelRequired()) {
			player.getDialogueManager().startDialogue(
					SimpleMessage.class,
					"You need a crafting level of " + gem.getLevelRequired()
							+ " to cut that gem.");
			return false;
		}
		if (!player.getInventory().containsOneItem(gem.getUncut())) {
			player.getDialogueManager().startDialogue(
					SimpleMessage.class,
					"You don't have any "
							+ ItemDefinitions
									.getItemDefinitions(gem.getUncut())
									.getName().toLowerCase() + " to cut.");
			return false;
		}
		return true;
	}

	@Override
	public boolean start(final Player player) {
		if (checkAll(player)) {
			setActionDelay(player, 1);
			player.setNextAnimation(new Animation(gem.getEmote()));
			return true;
		}
		return false;
	}

	@Override
	public boolean process(final Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(final Player player) {
		player.getInventory().deleteItem(gem.getUncut(), 1);
		player.getInventory().addItem(gem.getCut(), 1);
		player.getSkills().addXp(Skills.CRAFTING, gem.getExperience());
		player.getPackets().sendGameMessage(
				"You cut the "
						+ ItemDefinitions.getItemDefinitions(gem.getUncut())
								.getName().toLowerCase() + ".", true);
		quantity--;
		if (quantity <= 0)
			return -1;
		player.setNextAnimation(new Animation(gem.getEmote())); // start the
		// emote and add
		// 2 delay
		return 0;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}

	/**
	 * Enum for gems
	 *
	 * @author Raghav
	 */
	public enum Gem {
		OPAL(1625, 1609, 15.0, 1, 886),

		JADE(1627, 1611, 20, 13, 886),

		RED_TOPAZ(1629, 1613, 25, 16, 887),

		SAPPHIRE(1623, 1607, 40, 20, 888),

		EMERALD(1621, 1605, 57, 27, 889),

		RUBY(1619, 1603, 65, 34, 887),

		DIAMOND(1617, 1601, 57.5, 43, 890),

		DRAGONSTONE(1631, 1615, 107.5, 55, 885),

		ONYX(6571, 6573, 107.5, 67, 2717);

		private double experience;
		private int levelRequired;
		private int uncut, cut;

		private int emote;

		Gem(final int uncut, final int cut, final double experience,
			final int levelRequired, final int emote) {
			this.uncut = uncut;
			this.cut = cut;
			this.experience = experience;
			this.levelRequired = levelRequired;
			this.emote = emote;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public double getExperience() {
			return experience;
		}

		public int getUncut() {
			return uncut;
		}

		public int getCut() {
			return cut;
		}

		public int getEmote() {
			return emote;
		}

	}
}
