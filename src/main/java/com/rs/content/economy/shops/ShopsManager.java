package com.rs.content.economy.shops;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rs.content.customskills.sailing.ships.ShipShop;
import com.rs.content.economy.shops.impl.CoinShop;
import com.rs.content.economy.shops.impl.PointShop;
import com.rs.server.GameConstants;
import com.rs.utils.Logger;
import com.rs.player.Player;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class ShopsManager {

    private static final HashMap<Integer, Shop> SHOPS = new HashMap<>();
    private static final String PATH = GameConstants.DATA_PATH + "/shops/";

    public static void init() {
        SHOPS.clear();
        File[] files = new File(PATH).listFiles();
        if (files != null) {
            for (File f : files) {
                try {
                    Gson gson = new GsonBuilder().create();
                    ShopData shopData = gson.fromJson(new FileReader(f), ShopData.class);
                    if (shopData.getId() == 25) {
                        SHOPS.put(shopData.getId(), new ShipShop(shopData));
                        continue;
                    }
                    switch (shopData.getCurrency()) {
                        case GOLD:
                            SHOPS.put(shopData.getId(), new CoinShop(shopData));
                            break;
                        case PVM_POINTS:
                        case DONATE_POINTS:
                        case LOYALTY_POINTS:
                        case LEVEL_UP_POINTS:
                        case PK_POINTS:
                            SHOPS.put(shopData.getId(), new PointShop(shopData));
                            break;
                    }
                } catch (Exception e) {
                    Logger.info(ShopsManager.class, "ERROR @ SHOP " + f.getName() + e.getMessage());
                }
            }
            Logger.info(ShopsManager.class, "Loaded " + SHOPS.size() + " SHOPS.");
        }
    }

    public static void restoreShops() {
        SHOPS.values().forEach(Shop::restoreItems);
    }

    public static boolean handleShopNpc(Player player, int npcId) {
        int[] npcs;
        for (Shop shop : SHOPS.values()) {
            npcs = shop.shopData.getNpcIds();
            for (int nId : npcs) {
                if (nId == npcId) {
                    shop.addPlayer(player);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean openShop(final Player player, final int key) {
        final Shop shop = getShop(key);
        if (shop == null)
            return false;
        else {
            shop.addPlayer(player);
            return true;
        }
    }

    public static Shop getShop(final int key) {
        return SHOPS.get(key);
    }

}