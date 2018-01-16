package com.rs.content.actions.skills.crafting;

import com.rs.content.actions.Action;
import com.rs.content.actions.skills.Skills;
import com.rs.content.dialogues.impl.FlaxCraftingD;
import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.item.Item;

public class FlaxCrafting extends Action {

	private final Orb orb;
	private int quantity;

	public FlaxCrafting(final Orb orb, final int quantity) {
		this.orb = orb;
		this.quantity = quantity;
	}

	public static void make(final Player player, final Orb orb) {
		if (player.getInventory().getItems()
				.getNumberOf(new Item(orb.getUnMade(), 1)) <= 1) {
			// 1 lets start
			player.getActionManager().setAction(new FlaxCrafting(orb, 1));
		} else {
			player.getDialogueManager().startDialogue(FlaxCraftingD.class, orb);
		}
	}

	public boolean checkAll(final Player player) {
		if (player.getSkills().getLevel(Skills.CRAFTING) < orb
				.getLevelRequired()) {
			player.getDialogueManager().startDialogue(
					SimpleMessage.class,
					"You need a Crafting level of " + orb.getLevelRequired()
							+ " to make the bow string.");
			return false;
		}
		if (!player.getInventory().containsItem(1779, 1)) {
			player.getDialogueManager().startDialogue(SimpleMessage.class,
					"You need atleast one flax to make a bow string.");
			return false;
		}
		if (player.getInventory().containsOneItem(orb.getUnMade()))
			return true;
		return true;
	}

	@Override
	public boolean start(final Player player) {
		if (checkAll(player)) {
			setActionDelay(player, 4);
			player.setNextAnimation(new Animation(orb.getEmote()));
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
		player.getInventory().deleteItem(orb.getUnMade(), 1);
		player.getInventory().addItem(orb.getMade(), 1);
		player.getSkills().addXp(Skills.CRAFTING, orb.getExperience());
		player.getPackets().sendGameMessage(
				"You make the "
						+ ItemDefinitions.getItemDefinitions(orb.getUnMade())
								.getName().toLowerCase()
						+ " into a bow string.", true);
		quantity--;
		if (quantity <= 0)
			return -1;
		player.setNextAnimation(new Animation(orb.getEmote()));
		return 0;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}

	/**
	 * Enum for Flax | I was too bored to make new names for everything, ignore
	 * it.
	 *
	 * @author BongoProd
	 */
	public enum Orb {
		AIR_ORB(1779, 1777, 76, 1, 897, -1);

		private double experience;
		private int levelRequired;
		private int unmade, made;

		private int emote;
		private int gfxId;

		Orb(final int unmade, final int made, final double experience,
			final int levelRequired, final int emote, final int gfxId) {
			this.unmade = unmade;
			this.made = made;
			this.experience = experience;
			this.levelRequired = levelRequired;
			this.emote = emote;
			this.gfxId = gfxId;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public double getExperience() {
			return experience;
		}

		public int getUnMade() {
			return unmade;
		}

		public int getMade() {
			return made;
		}

		public int getEmote() {
			return emote;
		}

		public int getGfxId() {
			return gfxId;
		}

	}
}
