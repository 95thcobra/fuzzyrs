package com.rs.content.customskills.sailing.ships;

/**
 * @author John (FuzzyAvacado) on 12/12/2015.
 */
public enum Ships {

    NONE(-1, -1, 0, 0, 0, 0),
    CANOE(6969, 10, 1, 1, 500000, 1),
    SMALL_BOAT(5306, 20, 1, 1, 750000, 20),
    BATTLE_BOAT(8926, 30, 0, 3, 5000000, 40),
    CARGO_BOAT(43669, 50, 6, 0, 50000000, 75),
    SHIP(48579, 75, 3, 1, 100000000, 90);

    private final int id;
    private final int xOff;
    private final int yOff;
    private final int basePrice;
    private final int requirements;
    private final int storageSize;

    Ships(int id, int storageSize, int xOff, int yOff, int basePrice, int requirements) {
        this.id = id;
        this.storageSize = storageSize;
        this.xOff = xOff;
        this.yOff = yOff;
        this.basePrice = basePrice;
        this.requirements = requirements;
    }

    public int getId() {
        return id;
    }

    public int getXOffset() {
        return xOff;
    }

    public int getYOffset() {
        return yOff;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public int getRequirements() {
        return requirements;
    }

    public int getStorageSize() {
        return storageSize;
    }
}