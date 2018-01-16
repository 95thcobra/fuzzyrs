package com.rs.world.npc.familiar;

import com.rs.player.Player;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;
import com.rs.world.item.ItemConstants;
import com.rs.world.item.ItemsContainer;

import java.io.Serializable;

public class BeastOfBurden implements Serializable {

    private static final int ITEMS_KEY = 530;

    /**
     *
     */
    private static final long serialVersionUID = -2090871604834210257L;
    private final ItemsContainer<Item> beastItems;
    private transient Player player;
    private transient Familiar familiar;

    public BeastOfBurden(final int size) {
        beastItems = new ItemsContainer<>(size, false);
    }

    public void setEntitys(final Player player, final Familiar familiar) {
        setPlayer(player);
        setFamiliar(familiar);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setFamiliar(Familiar familiar) {
        this.familiar = familiar;
    }

    public void open() {
        player.getInterfaceManager().sendInterface(671);
        player.getInterfaceManager().sendInventoryInterface(665);
        sendInterItems();
        sendOptions();
    }

    public void dropBob() {
        final int size = familiar.getSize();
        final WorldTile WorldTile = new WorldTile(familiar.getCoordFaceX(size),
                familiar.getCoordFaceY(size), familiar.getPlane());
        for (int i = 0; i < beastItems.getSize(); i++) {
            final Item item = beastItems.get(i);
            if (item != null) {
                World.addGroundItem(item, WorldTile, player, false, -1, false);
            }
        }
        beastItems.reset();
    }

    public void takeBob() {
        final Item[] itemsBefore = beastItems.getItemsCopy();
        for (int i = 0; i < beastItems.getSize(); i++) {
            final Item item = beastItems.get(i);
            if (item != null) {
                if (!player.getInventory().addItem(item)) {
                    break;
                }
                beastItems.remove(i, item);
            }
        }
        beastItems.shift();
        refreshItems(itemsBefore);
    }

    public void removeItem(final int slot, final int amount) {
        Item item = beastItems.get(slot);
        if (item == null)
            return;
        final Item[] itemsBefore = beastItems.getItemsCopy();
        final int maxAmount = beastItems.getNumberOf(item);
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
        beastItems.remove(slot, item);
        beastItems.shift();
        player.getInventory().addItem(item);
        refreshItems(itemsBefore);
    }

    public void addItem(final int slot, final int amount) {
        Item item = player.getInventory().getItem(slot);
        if (item == null)
            return;
        if (!ItemConstants.isTradeable(item) || item.getId() == 4049 || (familiar != null && (familiar.canStoreEssOnly() && item.getId() != 1436 && item.getId() != 7936)) || item.getDefinitions().getGEPrice() > 1000000) {
            player.getPackets().sendGameMessage("You cannot store this item.");
            return;
        }
        final Item[] itemsBefore = beastItems.getItemsCopy();
        final int maxAmount = player.getInventory().getItems()
                .getNumberOf(item);
        if (amount < maxAmount) {
            item = new Item(item.getId(), amount);
        } else {
            item = new Item(item.getId(), maxAmount);
        }
        final int freeSpace = beastItems.getFreeSlots();
        if (!item.getDefinitions().isStackable()) {
            if (freeSpace == 0) {
                player.getPackets().sendGameMessage(
                        "Not enough space in your Familiar Inventory.");
                return;
            }

            if (freeSpace < item.getAmount()) {
                item.setAmount(freeSpace);
                player.getPackets().sendGameMessage(
                        "Not enough space in your Familiar Inventory.");
            }
        } else {
            if (freeSpace == 0 && !beastItems.containsOne(item)) {
                player.getPackets().sendGameMessage(
                        "Not enough space in your Familiar Inventory.");
                return;
            }
        }
        beastItems.add(item);
        beastItems.shift();
        player.getInventory().deleteItem(slot, item);
        refreshItems(itemsBefore);
    }

    public void refreshItems(final Item[] itemsBefore) {
        final int[] changedSlots = new int[itemsBefore.length];
        int count = 0;
        for (int index = 0; index < itemsBefore.length; index++) {
            final Item item = beastItems.getItems()[index];
            if (itemsBefore[index] != item) {
                changedSlots[count++] = index;
            }

        }
        final int[] finalChangedSlots = new int[count];
        System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
        refresh(finalChangedSlots);
    }

    public void refresh(final int... slots) {
        player.getPackets().sendUpdateItems(ITEMS_KEY, beastItems, slots);
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

    public boolean containsOneItem(final int... itemIds) {
        for (final int itemId : itemIds) {
            if (beastItems.containsOne(new Item(itemId, 1)))
                return true;
        }
        return false;
    }

    public void sendInterItems() {
        player.getPackets().sendItems(ITEMS_KEY, beastItems);
        player.getPackets().sendItems(93, player.getInventory().getItems());
    }

    public ItemsContainer<Item> getBeastItems() {
        return beastItems;
    }
}
