package com.rs.player;

import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.core.utils.Utils;
import com.rs.world.item.Item;
import com.rs.world.item.ItemConstants;

import java.io.Serializable;
import java.util.HashMap;

public class ChargesManager implements Serializable {

	private static final long serialVersionUID = -5978513415281726450L;
	private final HashMap<Integer, Integer> charges;
	private transient Player player;

	public ChargesManager() {
		charges = new HashMap<>();
	}

	public void setPlayer(final Player player) {
		this.player = player;
	}

	public void process() {
		final Item[] items = player.getEquipment().getItems().getItems();
		for (int slot = 0; slot < items.length; slot++) {
			final Item item = items[slot];
			if (item == null) {
				continue;
			}
			if (player.getAttackedByDelay() > Utils.currentTimeMillis()) {
				final int newId = ItemConstants
						.getDegradeItemWhenCombating(item.getId());
				if (newId != -1) {
					item.setId(newId);
					player.getEquipment().refresh(slot);
					player.getAppearance().generateAppearenceData();
					player.getPackets().sendGameMessage(
							"Your " + item.getDefinitions().getName()
							+ " degraded.");
				}
			}
			final int defaultCharges = ItemConstants.getItemDefaultCharges(item
					.getId());
			if (defaultCharges == -1) {
				continue;
			}
			if (ItemConstants.itemDegradesWhileWearing(item.getId())) {
				degrade(item.getId(), defaultCharges, slot);
			} else if (player.getAttackedByDelay() > Utils.currentTimeMillis()) {
				degrade(item.getId(), defaultCharges, slot);
			}
		}
	}

	public void die() {
		final Item[] equipItems = player.getEquipment().getItems().getItems();
		for (int slot = 0; slot < equipItems.length; slot++) {
			if (equipItems[slot] != null && degradeCompletly(equipItems[slot])) {
				player.getEquipment().getItems().set(slot, null);
			}
		}
		final Item[] invItems = player.getInventory().getItems().getItems();
		for (int slot = 0; slot < invItems.length; slot++) {
			if (invItems[slot] != null && degradeCompletly(invItems[slot])) {
				player.getInventory().getItems().set(slot, null);
			}
		}
	}

	/*
	 * return disapear;
	 */
	public boolean degradeCompletly(final Item item) {
		final int defaultCharges = ItemConstants.getItemDefaultCharges(item
				.getId());
		if (defaultCharges == -1)
			return false;
		while (true) {
			if (ItemConstants.itemDegradesWhileWearing(item.getId())
					|| ItemConstants.itemDegradesWhileCombating(item.getId())) {
				charges.remove(item.getId());
				final int newId = ItemConstants.getItemDegrade(item.getId());
				if (newId == -1)
					return ItemConstants.getItemDefaultCharges(item.getId()) == -1 ? false
							: true;
				item.setId(newId);
			} else {
				final int newId = ItemConstants.getItemDegrade(item.getId());
				if (newId != -1) {
					charges.remove(item.getId());
					item.setId(newId);
				}
				break;
			}
		}
		return false;
	}

	public void wear(final int slot) {
		final Item item = player.getEquipment().getItems().get(slot);
		if (item == null)
			return;
		final int newId = ItemConstants.getDegradeItemWhenWear(item.getId());
		if (newId == -1)
			return;
		player.getEquipment().getItems().set(slot, new Item(newId, 1));
		player.getEquipment().refresh(slot);
		player.getAppearance().generateAppearenceData();
		player.getPackets().sendGameMessage(
				"Your " + item.getDefinitions().getName() + " degraded.");
	}

	private void degrade(final int itemId, final int defaultCharges,
			final int slot) {
		Integer c = charges.remove(itemId);
		if (c == null) {
			c = defaultCharges;
		} else {
			c--;
			if (c == 0) {
				final int newId = ItemConstants.getItemDegrade(itemId);
				player.getEquipment().getItems()
				.set(slot, newId != -1 ? new Item(newId, 1) : null);
				if (newId == -1) {
					player.getPackets().sendGameMessage(
							"Your "
									+ ItemDefinitions
									.getItemDefinitions(itemId)
									.getName() + " became into dust.");
				} else {
					player.getPackets().sendGameMessage(
							"Your "
									+ ItemDefinitions
									.getItemDefinitions(itemId)
									.getName() + " degraded.");
				}
				player.getEquipment().refresh(slot);
				player.getAppearance().generateAppearenceData();
				return;
			}
		}
		charges.put(itemId, c);
	}

}
