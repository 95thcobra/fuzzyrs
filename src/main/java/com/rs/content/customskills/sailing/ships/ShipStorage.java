package com.rs.content.customskills.sailing.ships;

import com.rs.player.Player;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;
import com.rs.world.item.ItemsContainer;

/**
 * @author John (FuzzyAvacado) on 3/12/2016.
 */
public class ShipStorage {

    private static final int ITEMS_KEY = 531;

    private final ItemsContainer<Item> storageItems;
    private Player player;

    public ShipStorage(Player player, int storageSize) {
        this.player = player;
        storageItems = new ItemsContainer<>(storageSize, false);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ItemsContainer<Item> getStorageItems() {
        return storageItems;
    }

    public int getSize() {
        return storageItems.getUsedSlots();
    }

    public void empty() {
        storageItems.reset();
    }

    public void open() {
        player.getInterfaceManager().sendInterface(671);
        player.getInterfaceManager().sendInventoryInterface(665);
        sendInterItems();
        sendOptions();
    }

    public boolean containsItems(final Item[] items) {
        for (Item item : items)
            if (!storageItems.contains(item)) {
                return false;
            }
        return true;
    }

    public void sendInterItems() {
        player.getPackets().sendItems(ITEMS_KEY, storageItems);
        player.getPackets().sendItems(93, player.getInventory().getItems());
    }

    public void sendOptions() {
        player.getPackets().sendUnlockIComponentOptionSlots(665, 0, 0, 27, 0,
                1, 2, 3, 4, 5);
        player.getPackets().sendInterSetItemsOptionsScript(665, 0, 93, 4, 7,
                "Store", "Store-5", "Store-10", "Store-All", "Store-X",
                "Examine");
        player.getPackets().sendUnlockIComponentOptionSlots(671, 27, 0,
                ITEMS_KEY, 0, 1, 2, 3, 4, 5);
        player.getPackets().sendInterSetItemsOptionsScript(671, 27, ITEMS_KEY,
                6, 5, "Withdraw", "Withdraw-5", "Withdraw-10", "Withdraw-All",
                "Withdraw-X", "Examine");
    }

    public void dropStorage() {
        final int size = player.getSize();
        final WorldTile WorldTile = new WorldTile(player.getCoordFaceX(size), player.getCoordFaceY(size), player.getPlane());
        for (int i = 0; i < storageItems.getSize(); i++) {
            final Item item = storageItems.get(i);
            if (item != null) {
                World.addGroundItem(item, WorldTile, player, false, -1, false);
            }
        }
        storageItems.reset();
    }

    public void takeAll() {
        final Item[] itemsBefore = storageItems.getItemsCopy();
        for (int i = 0; i < storageItems.getSize(); i++) {
            final Item item = storageItems.get(i);
            if (item != null) {
                if (!player.getInventory().addItem(item)) {
                    break;
                }
                storageItems.remove(i, item);
            }
        }
        storageItems.shift();
        refreshItems(itemsBefore);
    }

    public void removeItem(final int slot, final int amount) {
        Item item = storageItems.get(slot);
        if (item == null)
            return;
        final Item[] itemsBefore = storageItems.getItemsCopy();
        final int maxAmount = storageItems.getNumberOf(item);
        if (amount < maxAmount) {
            item = new Item(item.getId(), amount);
        } else {
            item = new Item(item.getId(), maxAmount);
        }
        final int freeSpace = player.getInventory().getFreeSlots();
        if (!item.getDefinitions().isStackable()) {
            if (freeSpace == 0) {
                player.getPackets().sendGameMessage(
                        "Not enough space in your inventory.");
                return;
            }
            if (freeSpace < item.getAmount()) {
                item.setAmount(freeSpace);
                player.getPackets().sendGameMessage(
                        "Not enough space in your inventory.");
            }
        } else {
            if (freeSpace == 0
                    && !player.getInventory().containsItem(item.getId(), 1)) {
                player.getPackets().sendGameMessage(
                        "Not enough space in your inventory.");
                return;
            }
        }
        storageItems.remove(slot, item);
        storageItems.shift();
        player.getInventory().addItem(item);
        refreshItems(itemsBefore);
    }

    public void addItem(final int slot, final int amount) {
        Item item = player.getInventory().getItem(slot);
        if (item == null)
            return;
        final Item[] itemsBefore = storageItems.getItemsCopy();
        final int maxAmount = player.getInventory().getItems().getNumberOf(item);
        if (amount < maxAmount) {
            item = new Item(item.getId(), amount);
        } else {
            item = new Item(item.getId(), maxAmount);
        }
        final int freeSpace = storageItems.getFreeSlots();
        if (!item.getDefinitions().isStackable()) {
            if (freeSpace == 0) {
                player.getPackets().sendGameMessage("Not enough space in your ship's storage.");
                return;
            }
            if (freeSpace < item.getAmount()) {
                item.setAmount(freeSpace);
                player.getPackets().sendGameMessage("Not enough space in your ship's storage to store all of them.");
            }
        } else {
            if (freeSpace == 0 && !storageItems.containsOne(item)) {
                player.getPackets().sendGameMessage("Not enough space in your ship's storage.");
                return;
            }
        }
        storageItems.add(item);
        storageItems.shift();
        player.getInventory().deleteItem(slot, item);
        refreshItems(itemsBefore);
    }

    public boolean containsOneItem(final int... itemIds) {
        for (final int itemId : itemIds) {
            if (storageItems.containsOne(new Item(itemId, 1)))
                return true;
        }
        return false;
    }

    public void refreshItems(final Item[] itemsBefore) {
        final int[] changedSlots = new int[itemsBefore.length];
        int count = 0;
        for (int index = 0; index < itemsBefore.length; index++) {
            final Item item = storageItems.getItems()[index];
            if (itemsBefore[index] != item) {
                changedSlots[count++] = index;
            }
        }
        final int[] finalChangedSlots = new int[count];
        System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
        refresh(finalChangedSlots);
    }

    public void refresh(final int... slots) {
        player.getPackets().sendUpdateItems(ITEMS_KEY, storageItems, slots);
    }
}
