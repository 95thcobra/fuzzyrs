package com.rs.content.economy.shops;

/**
 * @author FuzzyAvacado
 */
public enum ShopCurrency {

    GOLD(995),
    PVM_POINTS,
    DONATE_POINTS,
    PK_POINTS,
    LOYALTY_POINTS,
    LEVEL_UP_POINTS;

    private final int id;

    ShopCurrency(int id) {
        this.id = id;
    }

    ShopCurrency() {
        this.id = -1;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.name().replace('_', ' ');
    }

}
