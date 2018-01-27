package com.rs.content.economy.exchange;

import com.rs.core.cache.loaders.ItemDefinitions.FileUtilities;
import com.rs.server.GameConstants;
import com.rs.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GrandExchangeUnlimitedItems {

    private final static String UNLIMITED_ITEMS = GameConstants.DATA_PATH + "/GE/unlimitedItems.txt";

    private static ArrayList<Integer> unlimitedItems = new ArrayList<Integer>();

    public static void init() {
        try {
            readToStoreCollection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readToStoreCollection() throws IOException {
        unlimitedItems.addAll(FileUtilities.readFile(UNLIMITED_ITEMS).stream().map(Integer::parseInt).collect(Collectors.toList()));
        Logger.info(GrandExchangeUnlimitedItems.class, "Initiated " + unlimitedItems.size() + " unlimited Items.");
    }

    public static ArrayList<Integer> getUnlimitedItems() {
        return unlimitedItems;
    }

    public static void reloadLimiteditems() {
        try {
            unlimitedItems.clear();
            readToStoreCollection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isUnlimited(int itemId) {
        for (Integer item : unlimitedItems) {
            if (itemId == item) {
                return true;
            }
        }
        return false;
    }

}
