package com.rs.world.npc;

public class Drop {

    @SuppressWarnings("unused")
    private final boolean rare;
    private int itemId, minAmount, maxAmount;
    private double rate;

    public Drop(final int itemId, final double rate, final int minAmount,
                final int maxAmount, final boolean rare) {
        this.itemId = itemId;
        this.rate = rate;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.rare = rare;
    }

    public static Drop create(final int itemId, final double rate,
                              final int minAmount, final int maxAmount, final boolean rare) {
        return new Drop((short) itemId, rate, minAmount, maxAmount, rare);
    }

    public int getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(final int amount) {
        this.minAmount = amount;
    }

    public int getExtraAmount() {
        return maxAmount - minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(final int amount) {
        this.maxAmount = amount;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(final short itemId) {
        this.itemId = itemId;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(final double rate) {
        this.rate = rate;
    }

    public boolean isFromRareTable() {
        return rare;
    }

}