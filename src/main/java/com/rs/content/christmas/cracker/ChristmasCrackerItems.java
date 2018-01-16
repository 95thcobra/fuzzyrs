package com.rs.content.christmas.cracker;

import com.rs.core.utils.Utils;
import com.rs.world.item.Item;

/**
 * @author John (FuzzyAvacado) on 12/11/2015.
 */
public class ChristmasCrackerItems {

    private final static Item[] PARTYHATS = {new Item(1038, 1), new Item(1040, 1),
            new Item(1042, 1), new Item(1044, 1), new Item(1046, 1),
            new Item(1048, 1)};

    private final static Item[] EXTRA_ITEMS = {new Item(1969, 1),
            new Item(2355, Utils.random(1, 2)), new Item(1217, 1),
            new Item(1635, 1), new Item(441, 5), new Item(441, 10),
            new Item(1973, 1), new Item(1718, 1), new Item(950, 1),
            new Item(563, 1), new Item(1987, 1)};

    public static Item getPartyhats() {
        return PARTYHATS[(int) (Math.random() * PARTYHATS.length)];
    }

    public static Item getExtraItems() {
        return EXTRA_ITEMS[(int) (Math.random() * EXTRA_ITEMS.length)];
    }
}
