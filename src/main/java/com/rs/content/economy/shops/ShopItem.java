package com.rs.content.economy.shops;

import com.rs.world.item.Item;

/**
 * @author FuzzyAvacado
 */
public class ShopItem extends Item {

    private final int price;

    public ShopItem(int itemId, int amount, int price) {
        super(itemId, amount);
        this.price = price;
    }

    public ShopItem(int itemId, int amount) {
        super(itemId, amount);
        this.price = getDefinitions().getGEPrice();
    }

    public int getPrice() {
        return price;
    }

}
