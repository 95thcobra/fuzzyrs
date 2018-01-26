package com.rs.content.economy.exchange;

import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.server.file.impl.GrandExchangeFileManager;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;
import com.rs.world.item.Item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author John (FuzzyAvacado) on 12/10/2015.
 */
public class GrandExchangePriceManager {

    public static final String DEFAULT_PRICE_PATH = GameConstants.DATA_PATH + "/items/prices/prices.txt";
    private static HashMap<Integer, Integer> prices;

    public static void init() throws IOException {
        File file = new File(GrandExchangeFileManager.GE_PRICES);
        if (file.exists()) {
            prices = GrandExchangeFileManager.loadGEPrices();
            if (prices != null) {
                Logger.info(GrandExchangePriceManager.class, "Loaded " + prices.size() + " prices for the Grand Exchange!");
                return;
            }
        }
        createNewPrices();
    }

    public static void calculatePrices() {
        ArrayList<OfferHistory> track = new ArrayList<>(GrandExchange.getOFFERS_TRACK());
        HashMap<Integer, BigInteger> averagePrice = new HashMap<>();
        HashMap<Integer, BigInteger> averageQuantity = new HashMap<>();
        for (OfferHistory o : track) {
            BigInteger price = averagePrice.get(o.getId());
            if (price != null) {
                BigInteger quantity = averageQuantity.get(o.getId());
                averagePrice.put(o.getId(), price.add(BigInteger.valueOf(o.getPrice())));
                averageQuantity.put(o.getId(), quantity.add(BigInteger.valueOf(o.getQuantity())));
            } else {
                averagePrice.put(o.getId(), BigInteger.valueOf(o.getPrice()));
                averageQuantity.put(o.getId(), BigInteger.valueOf(o.getQuantity()));
            }
        }
        BigInteger price;
        BigInteger quantity;
        long oldPrice, newPrice, min, max;
        for (int id : averagePrice.keySet()) {
            price = averagePrice.get(id);
            quantity = averageQuantity.get(id);
            oldPrice = getPrice(id);
            newPrice = price.divide(quantity).longValue();
            min = (long) ((double) oldPrice * 0.8D) - 100;
            max = (long) ((double) oldPrice * 1.2D) + 100;
            if (newPrice < min)
                newPrice = min;
            else if (newPrice > max)
                newPrice = max;

            if (newPrice < 1)
                newPrice = 1;
            else if (newPrice > Integer.MAX_VALUE)
                newPrice = Integer.MAX_VALUE;
            prices.put(id, (int) newPrice);
            setPrice(id, (int) newPrice);
        }
    }

    public static void createNewPrices() {
        if (prices == null) {
            prices = new HashMap<>();
        }
        prices.clear();
        final BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(DEFAULT_PRICE_PATH));
            String line;
            String[] data;
            int itemId, price;
            while ((line = in.readLine()) != null) {
                data = line.split(" - ");
                itemId = Integer.parseInt(data[0]);
                price = Integer.parseInt(data[1]);
                setPrice(itemId, price);
                prices.put(itemId, price);
            }
            Logger.info(GrandExchangePriceManager.class, "Created " + prices.size() + " prices for the Grand Exchange!");
            GrandExchangeFileManager.saveGEPrices(prices);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setPrice(int itemId, int price) {
        Item item = new Item(itemId);
        item.getDefinitions().setGEPrice(price);
    }

    public static Integer getPrice(int itemId) {
        return ItemDefinitions.getItemDefinitions(itemId).getGEPrice();
    }

    public static HashMap<Integer, Integer> getPrices() {
        return prices;
    }
}
