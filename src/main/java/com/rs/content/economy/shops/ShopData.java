package com.rs.content.economy.shops;

/**
 * @author FuzzyAvacado
 */
public class ShopData {

    private final int shopId;
    private final String name;
    private final ShopItem[] mainStock;
    private final ShopCurrency currency;
    private final boolean isGeneralStore;
    private final int[] npcIds;

    public ShopData(int shopId, String name, ShopItem[] mainStock, ShopCurrency currency, boolean isGeneralStore, int[] npcIds) {
        this.shopId = shopId;
        this.name = name;
        this.mainStock = mainStock;
        this.currency = currency;
        this.isGeneralStore = isGeneralStore;
        this.npcIds = npcIds;
    }

    public String getName() {
        return name;
    }

    public ShopItem[] getMainStock() {
        return mainStock;
    }

    public ShopCurrency getCurrency() {
        return currency;
    }

    public int getId() {
        return shopId;
    }

    public int[] getNpcIds() {
        return npcIds;
    }

    public ShopItem getShopItem(int itemId) {
        for (ShopItem shopItem : mainStock) {
            if (shopItem.getId() == itemId) {
                return shopItem;
            }
        }
        return null;
    }

    public boolean isGeneralStore() {
        return isGeneralStore;
    }
}
