package com.rs.server.file.impl;

import com.google.gson.reflect.TypeToken;
import com.rs.content.economy.exchange.Offer;
import com.rs.content.economy.exchange.OfferHistory;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;
import com.rs.server.file.JsonFileManager;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by John on 12/7/2015.
 */
@Getter(AccessLevel.PRIVATE)
public final class GrandExchangeFileManager extends JsonFileManager {

    public static final String GE_OFFERS = GameConstants.DATA_PATH + "/GE/grandexchange/grandExchangeOffers.json";
    public static final String GE_OFFERS_HISTORY = GameConstants.DATA_PATH + "/GE/grandexchange/grandExchangeOffersTrack.json";
    public static final String GE_PRICES = GameConstants.DATA_PATH + "/GE/grandexchange/grandExchangePrices.json";

    public synchronized HashMap<Long, Offer> loadGEOffers() {
        if (new File(GE_OFFERS).exists()) {
            try {
                return load(GE_OFFERS, new TypeToken<HashMap<Long, Offer>>() {}.getType());
            } catch (IOException e) {
                Logger.handle(e);
            }
        }
        return new HashMap<>();
    }

    public synchronized ArrayList<OfferHistory> loadGEHistory() {
        if (new File(GE_OFFERS_HISTORY).exists()) {
            try {
                return load(GE_OFFERS_HISTORY, new TypeToken<ArrayList<OfferHistory>>() {}.getType());
            } catch (IOException e) {
                Logger.handle(e);
            }
        }
        return new ArrayList<>();
    }

    public synchronized HashMap<Integer, Integer> loadGEPrices() {
        if (new File(GE_PRICES).exists()) {
            try {
                return load(GE_PRICES, new TypeToken<HashMap<Integer, Integer>>() {}.getType());
            } catch (IOException e) {
                Logger.handle(e);
            }
        }
        return new HashMap<>();
    }

    public synchronized void deleteOffers() {
        new File(GE_OFFERS).delete();
    }

    public synchronized void saveGEOffers(final HashMap<Long, Offer> offers) {
        try {
            save(GE_OFFERS, offers);
        } catch (IOException e) {
            Logger.handle(e);
        }
    }

    public synchronized void saveGEHistory(ArrayList<OfferHistory> history) {
        try {
            save(GE_OFFERS_HISTORY, history);
        } catch (IOException e) {
            Logger.handle(e);
        }
    }

    public synchronized void saveGEPrices(HashMap<Integer, Integer> prices) {
        try {
            save(GE_PRICES, prices);
        } catch (IOException e) {
            Logger.handle(e);
        }
    }
}
