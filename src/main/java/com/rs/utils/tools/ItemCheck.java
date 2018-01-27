package com.rs.utils.tools;

import com.rs.core.cache.Cache;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.utils.Utils;

import java.io.File;
import java.io.IOException;

public class ItemCheck {

    public static void main(final String[] args) throws IOException {
        Cache.init();
        int total = 0;
        for (int itemId = 0; itemId < Utils.getItemDefinitionsSize(); itemId++) {
            if (ItemDefinitions.getItemDefinitions(itemId).isWearItem(true)
                    && !ItemDefinitions.getItemDefinitions(itemId).isNoted()) {
                final File file = new File("bonuses/" + itemId + ".txt");
                if (!file.exists()) {
                    System.out.println(file.getName());
                    total++;
                }
            }
        }
        System.out.println("Total " + total);
    }
}
