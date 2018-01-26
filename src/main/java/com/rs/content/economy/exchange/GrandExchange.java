package com.rs.content.economy.exchange;

import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.server.file.impl.GrandExchangeFileManager;
import com.rs.core.utils.Utils;
import com.rs.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

public class GrandExchange {

    private static final Object LOCK = new Object();

    private static HashMap<Long, Offer> OFFERS;
    private static ArrayList<OfferHistory> OFFERS_TRACK;

    private static boolean edited;

    public static void init() {
        OFFERS = GrandExchangeFileManager.loadGEOffers();
        setOFFERS_TRACK(GrandExchangeFileManager.loadGEHistory());
    }

    public static void removeOffers(Player player) {
        for (long uid : player.getGeManager().getOfferUIds()) {
            if (uid == 0) {
                continue;
            }
            System.out.println("uid: " + uid);
            edited = true;
            OFFERS.remove(uid);
        }
        for (Entry<Long, Offer> entry : OFFERS.entrySet()) {
            Offer offer = entry.getValue();
            if (offer.getUsername().equals(player.getUsername())) {
                edited = true;
                OFFERS.remove(entry.getKey());
            }
        }
    }

    public static void removeAllOffers() {
        GrandExchangeFileManager.deleteOffers();
        OFFERS.clear();
    }

    public static int getTotalBuyQuantity(int itemId) {
        int quantity = 0;
        for (Offer offer : OFFERS.values()) {
            if (!offer.isBuying() || offer.getId() != itemId || offer.isCompleted())
                continue;
            quantity += offer.getAmount() - offer.getTotalAmmountSoFar();
        }
        return quantity;
    }

    public static int getTotalSellQuantity(int itemId) {
        int quantity = 0;
        for (Offer offer : OFFERS.values()) {
            if (offer.isBuying() || offer.getId() != itemId || offer.isCompleted())
                continue;
            quantity += offer.getAmount() - offer.getTotalAmmountSoFar();
        }
        return quantity;
    }

    public static void sendOfferTracker(Player player) {
        player.getInterfaceManager().sendInterface(275);
        int number = 0;
        for (int i = 0; i < 320; i++) {
            player.getPackets().sendIComponentText(275, i, "");
        }
        for (Offer offer : OFFERS.values()) {
            if (offer == null)
                continue;
            ItemDefinitions defs = ItemDefinitions.getItemDefinitions(offer.getId());
            if (offer.isCompleted())
                continue;
            player.getPackets().sendIComponentText(275, 1, "Grand Exchange Offers");
            int totalAmount = offer.getAmount() - offer.getTotalAmmountSoFar();
            player.getPackets().sendIComponentText(275, (13 + number++),
                    Utils.formatPlayerNameForDisplay(offer.getUsername()) + " ["
                            + (offer.isBuying() ? "Buying" : "Selling") + "] " + defs.getName() + " x "
                            + Utils.getFormattedNumber(totalAmount, ',') + " :  Price "
                            + Utils.getFormattedNumber(offer.getPrice(), ',') + " " + (totalAmount > 1 ? "each" : ""));
            player.getPackets().sendIComponentText(275, 11,
                    "Amount of Offers: " + number + "<br>It does only show offers above 250k price<br><br>");
            if (number >= 100) {
                break;
            }
        }
    }

    public static int getBestBuyPrice(int itemId) {
        int price = -1;
        for (Offer offer : OFFERS.values()) {
            if (!offer.isBuying() || offer.getId() != itemId || offer.isCompleted())
                continue;
            if (offer.getPrice() > price || price == -1) {
                price = offer.getPrice();
            }
        }
        return price;
    }

    public static int getBuyQuantity(int itemId) {
        int quantity = 0;
        for (Offer offer : OFFERS.values()) {
            if (!offer.isBuying() || offer.getId() != itemId || offer.isCompleted())
                continue;
            if (offer.getPrice() == getBestBuyPrice(itemId))
                quantity += offer.getAmount() - offer.getTotalAmmountSoFar();
        }
        return quantity;
    }

    public static int getCheapestSellPrice(int itemId) {
        int price = -1;
        for (Offer offer : OFFERS.values()) {
            if (offer.isBuying() || offer.getId() != itemId || offer.isCompleted())
                continue;
            if (offer.getPrice() < price || price == -1) {
                price = offer.getPrice();
            }
        }
        return price;
    }

    public static int getSellQuantity(int itemId) {
        int quantity = 0;
        for (Offer offer : OFFERS.values()) {
            if (offer.isBuying() || offer.getId() != itemId || offer.isCompleted())
                continue;
            if (offer.getPrice() == getCheapestSellPrice(itemId))
                quantity += offer.getAmount() - offer.getTotalAmmountSoFar();
        }
        return quantity;
    }

    public static void reset(boolean track, boolean price) {
        if (track)
            getOFFERS_TRACK().clear();
        if (price)
            GrandExchangePriceManager.createNewPrices();
    }

    public static void save() {
        if (!edited)
            return;
        GrandExchangeFileManager.saveGEOffers(OFFERS);
        GrandExchangeFileManager.saveGEHistory(getOFFERS_TRACK());
        edited = false;
    }

    public static void linkOffers(Player player) {
        boolean itemsWaiting = false;
        for (int slot = 0; slot < player.getGeManager().getOfferUIds().length; slot++) {
            Offer offer = getOffer(player, slot);
            if (offer == null)
                continue;
            offer.link(slot, player);
            offer.update();
            if (!itemsWaiting && offer.hasItemsWaiting()) {
                itemsWaiting = true;
                if (player.getSession() != null) {
                    player.getPackets().sendGameMessage("You have items from the Grand Exchange waiting in your collection box.");
                }
            }
        }
    }

    public static Offer getOffer(Player player, int slot) {
        synchronized (LOCK) {
            long uid = player.getGeManager().getOfferUIds()[slot];
            if (uid == 0)
                return null;
            Offer offer = OFFERS.get(uid);
            if (offer == null) {
                player.getGeManager().getOfferUIds()[slot] = 0;
                return null;
            }
            return offer;
        }

    }

    public static void submitOffer(Player player, int slot, int itemId, int amount, int price, boolean buy) {
        synchronized (LOCK) {
            Offer offer = new Offer(itemId, amount, price, buy);
            offer.setUsername(player.getUsername());
            player.getGeManager().getOfferUIds()[slot] = createOffer(offer);
            offer.link(slot, player);
            offer.update();
        }
    }

    public static void sendOffer(final Player player, final int slot, final int itemId, final int amount,
                                 final int price, final boolean buy) {
        synchronized (LOCK) {
            final Offer offer = new Offer(itemId, amount, price, buy);
            offer.setUsername(player.getUsername());
            player.getGeManager().getOfferUIds()[slot] = createOffer(offer);
            offer.link(slot, player);
            if (!offer.isBuying()) {
                if (GrandExchangeUnlimitedItems.isUnlimited(itemId))
                    offer.sellOffer(offer);
                else
                    findBuyerSeller(offer);
            } else {
                if (GrandExchangeUnlimitedItems.isUnlimited(itemId)) {
                    offer.buyOffer(offer);
                } else {
                    findBuyerSeller(offer);
                }
            }
        }
    }

    public static void abortOffer(Player player, int slot) {
        synchronized (LOCK) {
            Offer offer = getOffer(player, slot);
            if (offer == null)
                return;
            edited = true;
            if (offer.cancel() && offer.forceRemove())
                deleteOffer(player, slot);
        }
    }

    public static void collectItems(Player player, int slot, int invSlot, int option) {
        synchronized (LOCK) {
            Offer offer = getOffer(player, slot);
            if (offer == null)
                return;
            edited = true;
            if (offer.collectItems(invSlot, option) && offer.forceRemove()) {
                deleteOffer(player, slot);
                if (offer.getTotalAmmountSoFar() != 0) {
                    OfferHistory o = new OfferHistory(offer.getId(), offer.getTotalAmmountSoFar(),
                            offer.getTotalPriceSoFar(), offer.isBuying());
                    player.getGeManager().addOfferHistory(o);
                }
            }
        }
    }

    private static void deleteOffer(Player player, int slot) {
        player.getGeManager().cancelOffer();
        OFFERS.remove(player.getGeManager().getOfferUIds()[slot]);
        player.getGeManager().getOfferUIds()[slot] = 0;
    }

    private static void findBuyerSeller(final Offer offer) {
        while (!offer.isCompleted()) {
            Offer bestOffer = null;
            for (Offer o : OFFERS.values()) {
                if (o.isBuying() == offer.isBuying() || o.getId() != offer.getId() || o.isCompleted()
                        || (offer.isBuying() && o.getPrice() > offer.getPrice())
                        || (!offer.isBuying() && o.getPrice() < offer.getPrice()) || offer.isOfferTooHigh(o))
                    continue;
                if (bestOffer == null || (offer.isBuying() && o.getPrice() < bestOffer.getPrice())
                        || (!offer.isBuying() && o.getPrice() > bestOffer.getPrice()))
                    bestOffer = o;
            }
            if (bestOffer == null)
                break;
            offer.updateOffer(bestOffer);
        }
        offer.update();
    }

    private static long createOffer(Offer offer) {
        edited = true;
        long uid = getUId();
        OFFERS.put(uid, offer);
        return uid;
    }

    private static long getUId() {
        while (true) {
            long uid = new Random().nextLong();
            if (OFFERS.containsKey(uid))
                continue;
            return uid;
        }
    }

    public static void unlinkOffers(Player player) {
        for (int slot = 0; slot < player.getGeManager().getOfferUIds().length; slot++) {
            Offer offer = getOffer(player, slot);
            if (offer == null)
                continue;
            offer.unlink();
        }
    }

    public static List<OfferHistory> getHistory() {
        return getOFFERS_TRACK();
    }

    public static ArrayList<OfferHistory> getOFFERS_TRACK() {
        return OFFERS_TRACK;
    }

    public static void setOFFERS_TRACK(ArrayList<OfferHistory> OFFERS_TRACK) {
        GrandExchange.OFFERS_TRACK = OFFERS_TRACK;
    }
}