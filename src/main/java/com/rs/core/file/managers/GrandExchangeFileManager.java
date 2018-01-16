package com.rs.core.file.managers;

import com.rs.content.economy.exchange.Offer;
import com.rs.content.economy.exchange.OfferHistory;
import com.rs.core.file.GameFileManager;
import com.rs.core.file.DataFile;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by John on 12/7/2015.
 */
public class GrandExchangeFileManager {

    public static final String GE_OFFERS = GameConstants.DATA_PATH + "/GE/grandexchange/grandExchangeOffers.json";
    public static final String GE_OFFERS_HISTORY = GameConstants.DATA_PATH + "/GE/grandexchange/grandExchangeOffersTrack.json";
    public static final String GE_PRICES = GameConstants.DATA_PATH + "/GE/grandexchange/grandExchangePrices.json";

    public static synchronized HashMap<Long, Offer> loadGEOffers() {
        if (new File(GE_OFFERS).exists()) {
            try {
                return (HashMap<Long, Offer>) new DataFile<>(new File(GE_OFFERS), HashMap.class).fromJsonClass();
            } catch (IOException e) {
                Logger.handle(e);
            }
        }
        return new HashMap<>();
    }

    public static synchronized ArrayList<OfferHistory> loadGEHistory() {
        if (new File(GE_OFFERS_HISTORY).exists()) {
            try {
                return new DataFile<ArrayList<OfferHistory>>(new File(GE_OFFERS_HISTORY)).fromJson();
            } catch (IOException e) {
                Logger.handle(e);
            }
        }
        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public static synchronized HashMap<Integer, Integer> loadGEPrices() {
        if (new File(GE_PRICES).exists()) {
            try {
                return (HashMap<Integer, Integer>) GameFileManager.loadJsonFile(new File(GE_PRICES), HashMap.class);
            } catch (IOException | ClassNotFoundException e) {
                Logger.handle(e);
            }
        }
        return new HashMap<>();
    }

    public synchronized static void deleteOffers() {
        new File(GE_OFFERS).delete();
    }

    public static synchronized void saveGEOffers(final HashMap<Long, Offer> offers) {
        try {
            GameFileManager.storeJsonFile(offers, new File(GE_OFFERS), false);
        } catch (IOException e) {
            Logger.handle(e);
        }
    }

    public static synchronized void saveGEHistory(ArrayList<OfferHistory> history) {
        try {
            GameFileManager.storeJsonFile(history, new File(GE_OFFERS_HISTORY), false);
        } catch (IOException e) {
            Logger.handle(e);
        }
    }

    public static synchronized void saveGEPrices(HashMap<Integer, Integer> prices) {
        try {
            GameFileManager.storeJsonFile(prices, new File(GE_PRICES), false);
        } catch (IOException e) {
            Logger.handle(e);
        }
    }
}
