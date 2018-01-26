package com.rs.content.economy.shops.impl;

import com.rs.server.Server;
import com.rs.content.economy.shops.Shop;
import com.rs.content.economy.shops.ShopData;
import com.rs.content.economy.shops.ShopItem;
import com.rs.player.Player;
import com.rs.world.item.Item;

/**
 * @author FuzzyAvacado
 */
public class CoinShop extends Shop {

    public CoinShop(ShopData shopData) {
        super(shopData);
    }

    @Override
    public void handleBuy(Player player, int slotId, int quantity, ShopItem item) {
        int price = item.getPrice();
        int amountCoins = player.getInventory().getItems().getNumberOf(shopData.getCurrency().getId());
        int amountInPouch = player.getMoneyPouchValue();
        int maxPouch = amountInPouch / price;
        int maxQuantity = amountCoins / price;
        int buyQ = item.getAmount() > quantity ? quantity : item.getAmount();
        boolean enoughCoins = maxQuantity >= buyQ;
        boolean enoughInPouch = maxPouch >= buyQ;
        if (!enoughCoins && !enoughInPouch) {
            player.getPackets().sendGameMessage("You don't have enough coins.");
            buyQ = maxQuantity;
        } else if (quantity > buyQ) {
            player.getPackets().sendGameMessage("The shop has run out of stock.");
            if (item.getDefinitions().isStackable()) {
                if (player.getInventory().getFreeSlots() < 1) {
                    player.getPackets().sendGameMessage("Not enough space in your inventory.");
                    return;
                }
            }
        } else {
            int freeSlots = player.getInventory().getFreeSlots();
            if (!item.getDefinitions().isNoted() && buyQ > freeSlots) {
                buyQ = freeSlots;
                player.getPackets().sendGameMessage("Not enough space in your inventory.");
            }
        }
        if (buyQ != 0) {
            int totalPrice = price * buyQ;
            if (totalPrice > 0) {
                if (!player.hasMoney(price)) {
                    player.getPackets().sendGameMessage("You do not have enough coins to complete this purchase.");
                    return;
                } else {
                    player.takeMoney(price);
                    player.getInventory().addItem(item.getId(), buyQ);
                }
                item.setAmount(item.getAmount() - buyQ);
                if (item.getAmount() <= 0 && slotId >= shopData.getMainStock().length)
                    generalStock[slotId - shopData.getMainStock().length] = null;
                refreshShop();
                sendInventory(player);
                player.getPackets().sendGameMessage("You just bought " + buyQ + " " + item.getName() + " for " + totalPrice + " " + shopData.getCurrency().toString() + ".");
            } else {
                player.getPackets().sendGameMessage("You can't have more then 2147483647 coins in your inventory.");
            }
        }
    }

    @Override
    public void handleSell(Player player, int slotId, int quantity, Item item, int price, int originalId) {
        int actualPrice = (int) (price * Server.getInstance().getSettingsManager().getSettings().getSalesTax());
        int numberOf = player.getInventory().getItems().getNumberOf(originalId);
        if (quantity > numberOf)
            quantity = numberOf;
        if (!addItem(item.getId(), quantity)) {
            player.getPackets().sendGameMessage("Shop is currently full.");
            return;
        }
        int finalPrice = actualPrice * quantity;
        if (player.getMoneyPouchValue() + finalPrice > 0) {
            player.getInventory().deleteItem(originalId, quantity);
            player.getMoneyPouch().addMoney(finalPrice, false);
            player.getPackets().sendRunScript(5561, 1, finalPrice);
            player.refreshMoneyPouch();
        } else if (player.getInventory().getNumberOf(shopData.getCurrency().getId()) >= actualPrice) {
            player.getInventory().deleteItem(originalId, quantity);
            player.getInventory().addItem(shopData.getCurrency().getId(), finalPrice);
        } else {
            player.getPackets().sendGameMessage("You can't have more then 2147483647 coins in your pouch and inventory");
            return;
        }
        player.sendMessage("You sold " + quantity + " " + item.getName() + " for " + actualPrice + " " + shopData.getCurrency().toString());
    }

}
