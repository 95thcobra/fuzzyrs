package com.rs.content.economy.shops.impl;

import com.rs.content.economy.shops.Shop;
import com.rs.content.economy.shops.ShopData;
import com.rs.content.economy.shops.ShopItem;
import com.rs.content.player.points.PlayerPoints;
import com.rs.player.Player;
import com.rs.world.item.Item;

/**
 * @author John (FuzzyAvacado) on 12/22/2015.
 */
public class PointShop extends Shop {


    public PointShop(ShopData shopData) {
        super(shopData);
    }

    @Override
    public void handleBuy(Player player, int slotId, int quantity, ShopItem shopItem) {
        int dq = slotId >= shopData.getMainStock().length ? 0 : defaultQuantity[slotId];
        int price = shopItem.getPrice();
        int buyQ = shopItem.getAmount() > quantity ? quantity : shopItem.getAmount();
        PlayerPoints pointType = player.getPlayerPoints().getPoints(shopData.getCurrency());
        int pointAmount = player.getPlayerPoints().getPoints(pointType);
        int freeSlots = player.getInventory().getFreeSlots();
        if (!shopItem.getDefinitions().isNoted() && buyQ > freeSlots) {
            buyQ = freeSlots;
            player.getPackets().sendGameMessage("Not enough space in your inventory.");
        }
        if (buyQ != 0) {
            int totalPrice = price * buyQ;
            if (pointAmount >= totalPrice) {
                if (totalPrice > 0) {
                    player.getPlayerPoints().removePoints(pointType, totalPrice);
                    player.getInventory().addItem(shopItem.getId(), buyQ);
                    sendInventory(player);
                    player.getPackets().sendGameMessage("You just bought " + buyQ + " " + shopItem.getName() + " for " + totalPrice + " " + shopData.getCurrency().toString() + ".");
                }
            } else {
                player.sendMessage("You do not have enough " + shopData.getCurrency().toString() + " to complete this purchase!");
            }
        }
    }

    @Override
    public void handleSell(Player player, int slotId, int quantity, Item item, int price, int originalId) {
        if (!shopData.isGeneralStore()) {
            player.sendMessage("You cannot sell to this store!");
        } else {
            //TODO handle sell
        }
    }
}
