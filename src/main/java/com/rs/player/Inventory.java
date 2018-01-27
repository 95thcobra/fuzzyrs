package com.rs.player;

import com.rs.content.items.WeightManager;
import com.rs.utils.Utils;
import com.rs.utils.item.ItemExamines;
import com.rs.world.item.Item;
import com.rs.world.item.ItemsContainer;

import java.io.Serializable;

public final class Inventory implements Serializable {

    public static final int INVENTORY_INTERFACE = 679;
    private static final long serialVersionUID = 8842800123753277093L;
	private final ItemsContainer<Item> items;
	private transient Player player;

	public Inventory() {
		items = new ItemsContainer<Item>(28, false);
	}

	public void setPlayer(final Player player) {
		this.player = player;
	}

	public void init() {
		player.getPackets().sendItems(93, items);
	}

	public void unlockInventoryOptions() {
		player.getPackets().sendIComponentSettings(INVENTORY_INTERFACE, 0, 0,
				27, 4554126);
		player.getPackets().sendIComponentSettings(INVENTORY_INTERFACE, 0, 28,
				55, 2097152);
	}

	public void reset() {
		items.reset();
		init(); // as all slots reseted better just send all again
	}

	public void refresh(final int... slots) {
		player.getPackets().sendUpdateItems(93, items, slots);
        WeightManager.calculateWeight(player);
    }

	public boolean addItem(final int item, final int amount) {
		if (item < 0)
			return false;
		final int coinsInInventory = player.getInventory().getItems()
				.getNumberOf(995)
				+ amount;
		if (item == 995 && coinsInInventory < 0) {
			player.getPackets()
					.sendGameMessage(
							"You don't have enough space in your inventory to store this amount of money.");
			return false;
		}
		final int amount2 = items.getNumberOf(item);
		if ((amount2 + amount) < 0) {
			final int amount3 = (Integer.MAX_VALUE - amount2);
			if (amount3 <= 0) {
				player.getPackets().sendGameMessage(
						"Not enough space in your inventory");
				return true;
			}
			items.add(new Item(item, amount3));
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory");
			refresh();
			return true;
		}
		final boolean b = items.add(new Item(item, amount));
		if (!b) {
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return false;
		}
		refresh();
		return true;
	}

	public boolean addItem(final Item item) {
		if (item.getId() < 0
				|| item.getAmount() < 0
				|| !Utils.itemExists(item.getId())
				|| !player.getControllerManager().canAddInventoryItem(
						item.getId(), item.getAmount()))
			return false;
		final Item[] itemsBefore = items.getItemsCopy();
		if (!items.add(item)) {
			items.add(new Item(item.getId(), items.getFreeSlots()));
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			refreshItems(itemsBefore);
			return false;
		}
		refreshItems(itemsBefore);
		return true;
	}

	public void deleteItem(final int slot, final Item item) {
		if (!player.getControllerManager().canDeleteInventoryItem(item.getId(),
				item.getAmount()))
			return;
		final Item[] itemsBefore = items.getItemsCopy();
		items.remove(slot, item);
		refreshItems(itemsBefore);
	}

	public boolean removeItems(final Item... list) {
		for (final Item item : list) {
			if (item == null) {
				continue;
			}
			deleteItem(item);
		}
		return true;
	}

	public void deleteItem(final int itemId, final int amount) {
		if (!player.getControllerManager()
				.canDeleteInventoryItem(itemId, amount))
			return;
		final Item[] itemsBefore = items.getItemsCopy();
		items.remove(new Item(itemId, amount));
		refreshItems(itemsBefore);
	}

	public void deleteItem(final Item item) {
		if (!player.getControllerManager().canDeleteInventoryItem(item.getId(),
				item.getAmount()))
			return;
		final Item[] itemsBefore = items.getItemsCopy();
		items.remove(item);
		refreshItems(itemsBefore);
	}

	/*
	 * No refresh needed its client to who does it :p
	 */
	public void switchItem(final int fromSlot, final int toSlot) {
		final Item[] itemsBefore = items.getItemsCopy();
		final Item fromItem = items.get(fromSlot);
		final Item toItem = items.get(toSlot);
		items.set(fromSlot, toItem);
		items.set(toSlot, fromItem);
		refreshItems(itemsBefore);
	}

	public void refreshItems(final Item[] itemsBefore) {
		final int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			if (itemsBefore[index] != items.getItems()[index]) {
				changedSlots[count++] = index;
			}
		}
		final int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
	}

	public ItemsContainer<Item> getItems() {
		return items;
	}

	public boolean hasFreeSlots() {
		return items.getFreeSlot() != -1;
	}

	public int getFreeSlots() {
		return items.getFreeSlots();
	}

	public int getNumberOf(final int itemId) {
		return items.getNumberOf(itemId);
	}

	public Item getItem(final int slot) {
		return items.get(slot);
	}

	public int getItemsContainerSize() {
		return items.getSize();
	}

	public boolean containsItems(final Item[] item) {
		for (int i = 0; i < item.length; i++)
			if (!items.contains(item[i]))
				return false;
		return true;
	}

	public boolean containsItems(final int[] itemIds, final int[] ammounts) {
		final int size = itemIds.length > ammounts.length ? ammounts.length
				: itemIds.length;
		for (int i = 0; i < size; i++)
			if (!items.contains(new Item(itemIds[i], ammounts[i])))
				return false;
		return true;
	}

	public boolean containsItem(final int itemId, final int ammount) {
		return items.contains(new Item(itemId, ammount));
	}

	public boolean containsOneItem(final int... itemIds) {
		for (final int itemId : itemIds) {
			if (items.containsOne(new Item(itemId, 1)))
				return true;
		}
		return false;
	}

	public void sendExamine(final int slotId) {
		if (slotId >= getItemsContainerSize())
			return;
		final Item item = items.get(slotId);
		if (item == null)
			return;
		player.getPackets().sendInventoryMessage(0, slotId,
				ItemExamines.getExamine(item));
	}

	public void refresh() {
		player.getPackets().sendItems(93, items);
        WeightManager.calculateWeight(player);
    }

	public void viewOtherInventory(Player target) {
		ItemsContainer<Item> container = target.getInventory().getItems();
		player.getPackets().sendItems(93, container);
	}

}
