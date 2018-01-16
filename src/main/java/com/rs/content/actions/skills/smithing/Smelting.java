package com.rs.content.actions.skills.smithing;

import com.rs.content.actions.Action;
import com.rs.content.actions.skills.Skills;
import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.core.utils.Utils;
import com.rs.player.Equipment;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.WorldObject;
import com.rs.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class Smelting extends Action {

	public SmeltingBar bar;
	public WorldObject object;
	public int ticks;
	public Smelting(final int slotId, final WorldObject object, final int ticks) {
		this.object = object;
		this.bar = SmeltingBar.forId(slotId);
		this.ticks = ticks;
	}

	@Override
	public boolean start(final Player player) {
		if (bar == null || player == null || object == null)
			return false;
		if (!player.getInventory().containsItem(
				bar.getItemsRequired()[0].getId(),
				bar.getItemsRequired()[0].getAmount())) {
			player.getDialogueManager().startDialogue(
					SimpleMessage.class,
					"You need "
							+ bar.getItemsRequired()[0].getDefinitions()
							.getName() + " to create a "
							+ bar.getProducedBar().getDefinitions().getName()
							+ ".");
			return false;
		}
		if (bar.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItem(
					bar.getItemsRequired()[1].getId(),
					bar.getItemsRequired()[1].getAmount())) {
				player.getDialogueManager().startDialogue(
						SimpleMessage.class,
						"You need "
								+ bar.getItemsRequired()[1].getDefinitions()
								.getName()
								+ " to create a "
								+ bar.getProducedBar().getDefinitions()
								.getName() + ".");
				return false;
			}
		}
		if (player.getSkills().getLevel(Skills.SMITHING) < bar
				.getLevelRequired()) {
			player.getDialogueManager().startDialogue(
					SimpleMessage.class,
					"You need a Smithing level of at least "
							+ bar.getLevelRequired() + " to smelt "
							+ bar.getProducedBar().getDefinitions().getName());
			return false;
		}
		player.getPackets().sendGameMessage(
				"You place the required ores and attempt to create a bar of "
						+ bar.getProducedBar().getDefinitions().getName()
						.toLowerCase().replace(" bar", "") + ".", true);
		return true;
	}

	@Override
	public boolean process(final Player player) {
		if (bar == null || player == null || object == null)
			return false;
		if (!player.getInventory().containsItem(
				bar.getItemsRequired()[0].getId(),
				bar.getItemsRequired()[0].getAmount())) {
			player.getDialogueManager().startDialogue(
					SimpleMessage.class,
					"You need "
							+ bar.getItemsRequired()[0].getDefinitions()
							.getName() + " to create a "
							+ bar.getProducedBar().getDefinitions().getName()
							+ ".");
			return false;
		}
		if (bar.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItem(
					bar.getItemsRequired()[1].getId(),
					bar.getItemsRequired()[1].getAmount())) {
				player.getDialogueManager().startDialogue(
						SimpleMessage.class,
						"You need "
								+ bar.getItemsRequired()[1].getDefinitions()
								.getName()
								+ " to create a "
								+ bar.getProducedBar().getDefinitions()
								.getName() + ".");
				return false;
			}
		}
		if (player.getSkills().getLevel(Skills.SMITHING) < bar
				.getLevelRequired()) {
			player.getDialogueManager().startDialogue(
					SimpleMessage.class,
					"You need a Smithing level of at least "
							+ bar.getLevelRequired() + " to smelt "
							+ bar.getProducedBar().getDefinitions().getName());
			return false;
		}
		player.faceObject(object);
		return true;
	}

	public boolean isSuccessFull(final Player player) {
		return bar != SmeltingBar.IRON || player.getEquipment().getItem(Equipment.SLOT_RING) != null && player.getEquipment().getItem(Equipment.SLOT_RING).getId() == 2568 ||
				Utils.getRandom(100) <= (player.getSkills().getLevel(Skills.SMITHING) >= 45 ? 80 : 50);
	}

	@Override
	public int processWithDelay(final Player player) {
		ticks--;
		player.setNextAnimation(new Animation(3243));
		player.getSkills().addXp(Skills.SMITHING, bar.getExperience());
		for (final Item required : bar.getItemsRequired()) {
			player.getInventory().deleteItem(required.getId(),
					required.getAmount());
		}
		if (isSuccessFull(player)) {
			player.getInventory().addItem(bar.getProducedBar());
			player.getPackets().sendGameMessage(
					"You retrieve a bar of "
							+ bar.getProducedBar().getDefinitions().getName()
							.toLowerCase().replace(" bar", "") + ".",
							true);
		} else {
			player.getPackets().sendGameMessage(
					"The ore is too impure and you fail to refine it.", true);
		}
		if (ticks > 0)
			return 1;
		return -1;
	}

	@Override
	public void stop(final Player player) {
		this.setActionDelay(player, 3);
	}

	public enum SmeltingBar {

		BRONZE(1, 6.2, new Item[]{new Item(436), new Item(438)}, new Item(
				2349), 0),

		BLURITE(8, 8.0, new Item[]{new Item(668)}, new Item(9467), 1),

		IRON(15, 12.5, new Item[]{new Item(440)}, new Item(2351), 2),

		SILVER(20, 13.7, new Item[]{new Item(442)}, new Item(2355), 3),

		STEEL(30, 17.5, new Item[]{new Item(440), new Item(453, 2)},
				new Item(2353), 4),

		GOLD(40, 22.5, new Item[]{new Item(444)}, new Item(2357), 5),

		MITHRIL(50, 30, new Item[]{new Item(447), new Item(453, 4)},
				new Item(2359), 6),

		ADAMANT(70, 37.5, new Item[]{new Item(449), new Item(453, 6)},
				new Item(2361), 7),

		RUNE(85, 50, new Item[]{new Item(451), new Item(453, 8)}, new Item(
				2363), 8),

		DRAGONBANE(80, 50, new Item[]{new Item(21779)}, new Item(21783, 1),
				9),

		WALLASALKIBANE(80, 50, new Item[]{new Item(21780)}, new Item(21784,
				1), 10),

		BASILISKBANE(80, 50, new Item[]{new Item(21781)},
				new Item(21785, 1), 11), ABYSSSALBANE(80, 50,
				new Item[]{new Item(21782)}, new Item(21786, 1), 11);

		private static Map<Integer, SmeltingBar> bars = new HashMap<Integer, SmeltingBar>();

		static {
			for (final SmeltingBar bar : SmeltingBar.values()) {
				bars.put(bar.getButtonId(), bar);
			}
		}

		private int levelRequired;
		private double experience;
		private Item[] itemsRequired;
		private int buttonId;
		private Item producedBar;

		SmeltingBar(final int levelRequired, final double experience,
					final Item[] itemsRequired, final Item producedBar,
					final int buttonId) {
			this.levelRequired = levelRequired;
			this.experience = experience;
			this.itemsRequired = itemsRequired;
			this.producedBar = producedBar;
			this.buttonId = buttonId;
		}

		public static SmeltingBar forId(final int buttonId) {
			return bars.get(buttonId);
		}

		public Item[] getItemsRequired() {
			return itemsRequired;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public Item getProducedBar() {
			return producedBar;
		}

		public double getExperience() {
			return experience;
		}

		public int getButtonId() {
			return buttonId;
		}
	}
}
