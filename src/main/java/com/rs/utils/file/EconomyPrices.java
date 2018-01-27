package com.rs.utils.file;

import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.world.item.Item;
import com.rs.world.item.ItemConstants;

public final class EconomyPrices {

    private EconomyPrices() {

    }

    public static int getPrice(int itemId) {
        final ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
        if (defs.isNoted()) {
            itemId = defs.getCertId();
        } else if (defs.isLended()) {
            itemId = defs.getLendId();
        }
        if (!ItemConstants.isTradeable(new Item(itemId, 1)))
            return 0;
        if (itemId == 995) // TODO after here
            return 1;
        return defs.getGEPrice(); // TODO get price from real item from saved
        // prices from ge
    }
}
