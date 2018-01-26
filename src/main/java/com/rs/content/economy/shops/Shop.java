package com.rs.content.economy.shops;

import com.rs.server.Server;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.core.utils.item.ItemExamines;
import com.rs.core.utils.item.ItemSetsKeyGenerator;
import com.rs.player.Player;
import com.rs.world.item.Item;
import com.rs.world.item.ItemConstants;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Shop {

    public static final int MAIN_STOCK_ITEMS_KEY = ItemSetsKeyGenerator
            .generateKey();

    public static final int MAX_SHOP_ITEMS = 40;

    public static final int SHOP_INTERFACE_ID = 1265;

    public final ShopData shopData;
    public final CopyOnWriteArrayList<Player> viewingPlayers;
    public final int[] defaultQuantity;
    public int quantity;
    public ShopItem[] generalStock;

    public Shop(ShopData shopData) {
        this.viewingPlayers = new CopyOnWriteArrayList<>();
        this.shopData = shopData;
        this.defaultQuantity = new int[shopData.getMainStock().length];
        for (int i = 0; i < this.defaultQuantity.length; i++) {
            this.defaultQuantity[i] = shopData.getMainStock()[i].getAmount();
        }
        if (shopData.isGeneralStore() && shopData.getMainStock().length < MAX_SHOP_ITEMS)
            generalStock = new ShopItem[MAX_SHOP_ITEMS - shopData.getMainStock().length];
    }

    public abstract void handleBuy(final Player player, final int slotId, final int quantity, final ShopItem shopItem);

    public abstract void handleSell(final Player player, final int slotId, final int quantity, final Item item, final int price, final int originalId);

    public void buy(final Player player, final int slotId, final int quantity) {
        if (slotId >= getStoreSize())
            return;
        ShopItem item = slotId >= shopData.getMainStock().length ? generalStock[slotId
                - shopData.getMainStock().length] : shopData.getMainStock()[slotId];
        if (item == null)
            return;
        if (item.getAmount() == 0) {
            player.getPackets().sendGameMessage(
                    "There is no stock of that item at the moment.");
            return;
        }
        handleBuy(player, slotId, quantity, item);
    }

    public void sell(final Player player, final int slotId, final int quantity) {
        if (player.getInventory().getItemsContainerSize() < slotId)
            return;
        Item item = player.getInventory().getItem(slotId);
        if (item == null)
            return;
        int price = shopData.getShopItem(item.getId()) != null ? shopData.getShopItem(item.getId()).getPrice() : item.getDefinitions().getGEPrice();
        int originalId = item.getId();
        if (item.getDefinitions().isNoted())
            item = new Item(item.getDefinitions().getCertId(), item.getAmount());
        if (item.getDefinitions().isDestroyItem()
                || ItemConstants.getItemDefaultCharges(item.getId()) != -1
                || !ItemConstants.isTradeable(item) || item.getId() == shopData.getCurrency().getId()) {
            player.getPackets().sendGameMessage("You can't sell this item.");
            return;
        }
        int dq = getDefaultQuantity(item.getId());
        if (dq == -1 && generalStock == null) {
            player.getPackets().sendGameMessage(
                    "You can't sell this item to this shop.");
            return;
        }
        handleSell(player, slotId, quantity, item, price, originalId);
    }

    public void sendValue(Player player, int slotId) {
        if (player.getInventory().getItemsContainerSize() < slotId)
            return;
        Item item = player.getInventory().getItem(slotId);
        if (item == null)
            return;
        if (item.getDefinitions().isNoted())
            item = new Item(item.getDefinitions().getCertId(), item.getAmount());
        if (item.getDefinitions().isNoted() || !ItemConstants.isTradeable(item)
                || item.getId() == shopData.getCurrency().getId()) {
            player.getPackets().sendGameMessage("You can't sell this item.");
            return;
        }
        int dq = getDefaultQuantity(item.getId());
        if (dq == -1 && generalStock == null) {
            player.getPackets().sendGameMessage(
                    "You can't sell this item to this shop.");
            return;
        }
        int price = (int) (Server.getInstance().getSettingsManager().getSettings().getSalesTax() * (shopData.getShopItem(item.getId()) != null ? shopData.getShopItem(item.getId()).getPrice() : item.getDefinitions().getGEPrice()));
        player.getPackets().sendGameMessage(item.getDefinitions().getName() + ": " + shopData.getName() + " will buy for: "
                + price + " " + shopData.getCurrency().toString() + ".");
    }

    public int getDefaultQuantity(int itemId) {
        for (int i = 0; i < shopData.getMainStock().length; i++)
            if (shopData.getMainStock()[i].getId() == itemId)
                return defaultQuantity[i];
        return -1;
    }

    public void addPlayer(final Player player) {
        viewingPlayers.add(player);
        player.getTemporaryAttributtes().put("Shop", this);
        player.setCloseInterfacesEvent(() -> {
            viewingPlayers.remove(player);
            player.getTemporaryAttributtes().remove("Shop");
        });
        player.getPackets().sendConfig(118, MAIN_STOCK_ITEMS_KEY);
        sendStore(player);
        player.getInterfaceManager().sendInterface(SHOP_INTERFACE_ID); // opens shop
        player.getPackets().sendConfig(1496, 555);// unkown
        player.getPackets().sendConfig(532, shopData.getCurrency().getId());// unlocks money amount in inv
        player.getPackets().sendIComponentSettings(SHOP_INTERFACE_ID, 20, 0, getStoreSize() * 6, 1150); // unlocks stock slots
        player.getPackets().sendIComponentSettings(SHOP_INTERFACE_ID, 26, 0, getStoreSize() * 6, 82903066); // unlocks drag item to inv
        sendInventory(player);
        quantity = 1;
        player.getPackets().sendIComponentText(SHOP_INTERFACE_ID, 85, shopData.getName());
        player.getPackets().sendIComponentText(SHOP_INTERFACE_ID, 212, "+10");
        player.getPackets().sendIComponentText(SHOP_INTERFACE_ID, 221, "-10");
    }

    public void sendInventory(Player player) {
        player.getInterfaceManager().sendInventoryInterface(1266);
        player.getPackets().sendItems(93, player.getInventory().getItems());
        player.getPackets().sendUnlockIComponentOptionSlots(1266, 0, 0, 28, 0,
                1, 2, 3, 4, 5);
        player.getPackets().sendInterSetItemsOptionsScript(1266, 0, 93, 4, 7,
                "Value", "Sell 1", "Sell 5", "Sell 10", "Sell 50", "Sell 500", "Examine");
    }

    public void restoreItems() {
        boolean needRefresh = false;
        for (int i = 0; i < shopData.getMainStock().length; i++) {
            if (shopData.getMainStock()[i].getAmount() < defaultQuantity[i]) {
                shopData.getMainStock()[i].setAmount(shopData.getMainStock()[i].getAmount() + 1);
                needRefresh = true;
            } else if (shopData.getMainStock()[i].getAmount() > defaultQuantity[i]) {
                shopData.getMainStock()[i].setAmount(shopData.getMainStock()[i].getAmount() + -1);
                needRefresh = true;
            }
        }
        if (generalStock != null) {
            for (int i = 0; i < generalStock.length; i++) {
                ShopItem item = generalStock[i];
                if (item == null)
                    continue;
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() <= 0)
                    generalStock[i] = null;
                needRefresh = true;
            }
        }
        if (needRefresh)
            refreshShop();
    }

    protected boolean addItem(final int itemId, final int quantity) {
        for (ShopItem item : shopData.getMainStock()) {
            if (item.getId() == itemId) {
                item.setAmount(item.getAmount() + quantity);
                refreshShop();
                return true;
            }
        }
        if (generalStock != null) {
            for (ShopItem item : generalStock) {
                if (item == null)
                    continue;
                if (item.getId() == itemId) {
                    item.setAmount(item.getAmount() + quantity);
                    refreshShop();
                    return true;
                }
            }
            for (int i = 0; i < generalStock.length; i++) {
                if (generalStock[i] == null) {
                    generalStock[i] = new ShopItem(itemId, quantity);
                    refreshShop();
                    return true;
                }
            }
        }
        return false;
    }

    public void sendInfo(final Player player, final int slotId) {
        if (player.isBuying) {
            if (slotId >= getStoreSize())
                return;
            ShopItem item = slotId >= shopData.getMainStock().length ? generalStock[slotId
                    - shopData.getMainStock().length] : shopData.getMainStock()[slotId];
            if (item == null)
                return;
            player.getTemporaryAttributtes().put("BuySelectedSlot", slotId);
            quantity = 1;
            player.getPackets().sendConfig(2561, MAIN_STOCK_ITEMS_KEY);
            player.getPackets().sendGlobalConfig(1876, ItemDefinitions.getItemDefinitions(item.getId()).getEquipSlot());
            player.getPackets().sendConfig(2562, item.getId());
            player.getPackets().sendConfig(2563, slotId);
            player.getPackets().sendConfig(2564, quantity);
            player.getPackets().sendIComponentText(SHOP_INTERFACE_ID, 40, ItemExamines.getExamine(item));
            player.getPackets().sendIComponentText(SHOP_INTERFACE_ID, 43, "This is " + item.getDefinitions().getItemType() + ".");
            if (shopData.isGeneralStore()) {
                player.getPackets().sendHideIComponent(SHOP_INTERFACE_ID, 52, false);// generalstore icon
            }
            int price = item.getPrice();
            player.getPackets().sendGameMessage(item.getDefinitions().getName() + ": " + shopData.getName() + " will sell for: " + price + " " + shopData.getCurrency().toString()
                    + ".");
        } else {
            Item item = player.getInventory().getItem(slotId);
            if (item == null)
                return;
            player.getTemporaryAttributtes().put("SellSelectedSlot", slotId);
            quantity = 1;
            player.getPackets().sendConfig(2561, MAIN_STOCK_ITEMS_KEY);
            player.getPackets().sendGlobalConfig(1876, ItemDefinitions.getItemDefinitions(item.getId()).getEquipSlot());
            player.getPackets().sendConfig(2562, item.getId());
            player.getPackets().sendConfig(2563, slotId);
            player.getPackets().sendConfig(2564, quantity);
            player.getPackets().sendIComponentText(SHOP_INTERFACE_ID, 40, ItemExamines.getExamine(item));
            player.getPackets().sendIComponentText(SHOP_INTERFACE_ID, 43, "This is " + item.getDefinitions().getItemType() + ".");
            if (item.getDefinitions().isWearItem()) {
                player.getPackets().sendIComponentText(SHOP_INTERFACE_ID, 44, "It is " + item.getDefinitions().getEquipmentType() + ".");
            }
            int price = (int) (Server.getInstance().getSettingsManager().getSettings().getSalesTax() * (shopData.getShopItem(item.getId()) != null ? shopData.getShopItem(item.getId()).getPrice() : item.getDefinitions().getGEPrice()));
            player.getPackets().sendGameMessage(item.getDefinitions().getName() + ": " + shopData.getName() + " will buy for: " + price + " "
                    + shopData.getCurrency().toString()
                            + ".");
        }
    }

    public void refreshShop() {
        for (Player player : viewingPlayers) {
            sendStore(player);
            player.getPackets().sendIComponentSettings(SHOP_INTERFACE_ID, 20, 0, getStoreSize() * 6, 1150);
        }
    }

    public int getStoreSize() {
        return shopData.getMainStock().length + (generalStock != null ? generalStock.length : 0);
    }

    public void sendStore(final Player player) {
        Item[] stock = new Item[shopData.getMainStock().length
                + (generalStock != null ? generalStock.length : 0)];
        System.arraycopy(shopData.getMainStock(), 0, stock, 0, shopData.getMainStock().length);
        if (generalStock != null)
            System.arraycopy(generalStock, 0, stock, shopData.getMainStock().length, generalStock.length);
        player.getPackets().sendItems(MAIN_STOCK_ITEMS_KEY, stock);
    }

}