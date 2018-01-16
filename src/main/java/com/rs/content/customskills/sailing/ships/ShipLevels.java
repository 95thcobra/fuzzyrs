package com.rs.content.customskills.sailing.ships;

/**
 * @author John (FuzzyAvacado) on 12/15/2015.
 */
public enum ShipLevels {

    DEFENSE("WOOD", "BRONZE", "IRON", "STEEL", "MITHRIL", "ADAMANT", "RUNE", "DRAGON", "TITANIUM", "DIAMOND"),
    ATTACK("WOOD", "BRONZE", "IRON", "STEEL", "MITHRIL", "ADAMANT", "RUNE", "DRAGON", "TITANIUM", "DIAMOND"),
    STEALTH("NOTHING", "CAMOUFLAGE", "CLOAKED");

    private final String[] names;

    ShipLevels(String... names) {
        this.names = names;
    }

    public String[] getNames() {
        return names;
    }

    public String getName(int ordinal) {
        return names[ordinal];
    }

}
