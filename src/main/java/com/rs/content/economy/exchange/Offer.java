package com.rs.content.economy.exchange;

import com.rs.core.cache.loaders.ClientScriptMap;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.item.Item;
import com.rs.world.item.ItemsContainer;

public class Offer extends Item {

    // offer will contain the owner, its the most efficient way to update in
    // terms speed
    // owner needs to be deleted when logs out from here else it caches player
    // on memory
    private transient Player owner;
    private transient int slot;
    private int price; // price per item selling or buying
    private int totalPriceSoFar; // total profit received so far or spent
    private int totalAmmountSoFar; // amt of items sold or bought
    private ItemsContainer<Item> receivedItems;
    private boolean canceled;
    private boolean buying;
    private String username;

    public Offer(int id, int amount, int price, boolean buy) {
        super(id, amount);
        this.price = price;
        buying = buy;
        receivedItems = new ItemsContainer<>(2, true);
    }

    public void link(int slot, Player owner) {
        this.slot = slot;
        this.setOwner(owner);
    }

    public void unlink() {
        setOwner(null);
    }

    public void update() {
        if (getOwner() == null)
            return;
        if (getOwner().getSession() != null) {
            getOwner().getPackets().sendGrandExchangeOffer(this);
            sendItems();
        }
    }

    public void sendItems() {
        if (getOwner() == null)
            return;
        getOwner().getPackets().sendItems(
                ClientScriptMap.getMap(1079).getIntValue(slot), receivedItems);
    }

    public int getPrice() {
        return price;
    }

    public boolean forceRemove() {
        return isCompleted() && !hasItemsWaiting();
    }

    protected boolean isCompleted() {
        return canceled || totalAmmountSoFar >= getAmount();
    }

    public int getPercentage() {
        return totalAmmountSoFar * getAmount() / 100;
    }

    public int getTotalAmmountSoFar() {
        return totalAmmountSoFar;
    }

    public int getTotalPriceSoFar() {
        return totalPriceSoFar;
    }

    public int getSlot() {
        return slot;
    }

    public int getStage() {
        if (forceRemove())
            return 0;
        if (isCompleted())
            return buying ? 5 : 13;
        return buying ? 2 : 10;
    }

    public boolean isBuying() {
        return buying;
    }

    // TODO canceling message
    public boolean cancel() {
        if (isCompleted())
            return false;
        canceled = true;
        if (buying)
            receivedItems.add(new Item(995, (getAmount() - totalAmmountSoFar)
                    * price));
        else
            receivedItems
                    .add(new Item(getId(), getAmount() - totalAmmountSoFar));
        update();
        return true;
    }

    public void sendUpdateWarning(Offer offer) {
        if (getOwner() == null)
            return;
        if (!isCompleted()) {
            if (offer.isBuying())
                getOwner().getPackets().sendGameMessage(
                        "Grand Exchange: Bought "
                                + Utils.getFormattedNumber(
                                offer.getTotalAmmountSoFar(), ',')
                                + "/"
                                + Utils.getFormattedNumber(offer.getAmount(),
                                ',') + " x "
                                + offer.getDefinitions().getName());
            else
                getOwner().getPackets().sendGameMessage(
                        "Grand Exchange: Sold "
                                + Utils.getFormattedNumber(
                                offer.getTotalAmmountSoFar(), ',')
                                + "/"
                                + Utils.getFormattedNumber(offer.getAmount(),
                                ',') + " x "
                                + offer.getDefinitions().getName());
        } else
            getOwner()
                    .getPackets()
                    .sendGameMessage(
                            "One or more of your Grand Exchange offers have been updated.");
        getOwner().getPackets().sendMusicEffect(284);
        update();
    }

    public boolean isOfferTooHigh(Offer fromOffer) {
        int left = getAmount() - totalAmmountSoFar;
        int leftFrom = fromOffer.getAmount() - fromOffer.totalAmmountSoFar;
        int exchangeAmt = left > leftFrom ? leftFrom : left;
        int totalPrice = exchangeAmt * fromOffer.price;
        int amtCoins = receivedItems.getNumberOf(995);

        if (buying) {
            if (fromOffer.receivedItems.getNumberOf(995) + totalPrice <= 0)
                return true;
            int leftcoins = exchangeAmt * price - totalPrice;
            if (leftcoins > 0 && amtCoins + leftcoins <= 0)
                return true;
        } else {
            if (amtCoins + totalPrice <= 0)
                return true;
        }
        return false;

    }

    public void sellOffer(Offer fromOffer) {
        int exchangeAmt = getAmount();
        int totalPrice = getPrice() * exchangeAmt;
        int fiveProcentPrice = (int) ((Math.ceil(GrandExchangePriceManager.getPrice(fromOffer.getId())) * 0.95) * exchangeAmt);
        receivedItems.add(new Item(995, fiveProcentPrice));
        totalAmmountSoFar += exchangeAmt;
        totalPriceSoFar += totalPrice;
        sendUpdateWarning(fromOffer);
    }

    public void buyOffer(Offer fromOffer) {
        int left = getAmount() - totalAmmountSoFar;
        int leftFrom = fromOffer.getAmount() - fromOffer.totalAmmountSoFar;
        int exchangeAmt = left > leftFrom ? leftFrom : left;
        int totalPrice = exchangeAmt * GrandExchangePriceManager.getPrice(fromOffer.getId());
        int leftcoins = exchangeAmt * price - totalPrice;
        if (leftcoins > 0) {
            receivedItems.add(new Item(995, leftcoins));
        }
        receivedItems.add(buying ? new Item(getId(), exchangeAmt) : new Item(getId(), exchangeAmt));
        totalAmmountSoFar += exchangeAmt;
        totalPriceSoFar += totalPrice;
        sendUpdateWarning(fromOffer);
    }

    public void instaOffer(Offer fromOffer) {
        int left = getAmount() - totalAmmountSoFar;
        int leftFrom = fromOffer.getAmount() - fromOffer.totalAmmountSoFar;
        int exchangeAmt = left > leftFrom ? leftFrom : left;
        int totalPrice = exchangeAmt * fromOffer.price;
        if (buying) {
            int leftcoins = exchangeAmt * price - totalPrice;
            if (leftcoins > 0) {
                receivedItems.add(new Item(995, leftcoins));
            }
            receivedItems.add(buying ? new Item(getId(), exchangeAmt)
                    : new Item(getId(), exchangeAmt));
        } else {
            receivedItems.add(new Item(995, totalPrice));
        }
        totalAmmountSoFar += exchangeAmt;
        totalPriceSoFar += totalPrice;
    }

    public void updateOffer(Offer fromOffer) {
        int left = getAmount() - totalAmmountSoFar;
        int leftFrom = fromOffer.getAmount() - fromOffer.totalAmmountSoFar;
        int exchangeAmt = left > leftFrom ? leftFrom : left;
        int totalPrice = exchangeAmt * fromOffer.price;
        if (buying) {
            int leftcoins = exchangeAmt * price - totalPrice;
            if (leftcoins > 0) {
                receivedItems.add(new Item(995, leftcoins));
            }
            receivedItems.add(buying ? new Item(getId(), exchangeAmt)
                    : new Item(getId(), exchangeAmt));
            fromOffer.receivedItems.add(new Item(995, totalPrice));
        } else {
            fromOffer.receivedItems.add(new Item(getId(), exchangeAmt));
            receivedItems.add(new Item(995, totalPrice));
        }
        totalAmmountSoFar += exchangeAmt;
        fromOffer.totalAmmountSoFar += exchangeAmt;
        totalPriceSoFar += totalPrice;
        fromOffer.totalPriceSoFar += totalPrice;
        sendUpdateWarning(this);
        fromOffer.sendUpdateWarning(fromOffer);
    }

    public boolean collectItems(int slot, int option) {
        if (getOwner() == null)
            return false;
        int freeSlots = getOwner().getInventory().getFreeSlots();
        if (freeSlots == 0) {
            getOwner().getPackets().sendGameMessage(
                    "Not enough space in your inventory.");
            return false;
        }
        Item item = receivedItems.get(slot);
        if (item == null)
            return false;
        ItemDefinitions defs = item.getDefinitions();
        if (defs.getId() == 995) {
            getOwner().getMoneyPouch().addMoney(item.getAmount(), false);
            receivedItems.remove(item);
        } else if (defs.isStackable()
                && getOwner().getInventory().getNumberOf(item.getId())
                + item.getAmount() < 0) {
            Item add = new Item(item.getId(), Integer.MAX_VALUE
                    - getOwner().getInventory().getNumberOf(item.getId()));
            Item add1 = new Item(item.getId(), Integer.MAX_VALUE);
            receivedItems.remove(add);
            getOwner().getInventory().deleteItem(add1);
            getOwner().getInventory().addItem(add1);
            getOwner().getPackets().sendGameMessage(
                    "Not enough space in your inventory.");
        } else if (!defs.isStackable()
                && option == (item.getAmount() == 1 ? 0 : 1)) {
            Item add = new Item(item.getId(),
                    item.getAmount() > freeSlots ? freeSlots : item.getAmount());
            getOwner().getInventory().addItem(add);
            receivedItems.remove(add);
        } else {
            getOwner().getInventory().addItem(
                    new Item(defs.getCertId() != -1 ? defs.getCertId() : item.getId(),
                            item.getAmount()));
            receivedItems.remove(item);
        }
        update();
        return true;
    }

    public boolean hasItemsWaiting() {
        return receivedItems.getFreeSlots() != 2;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
