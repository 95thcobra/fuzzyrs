package com.rs.player;

import com.rs.server.Server;
import com.rs.content.actions.skills.prayer.GildedAltar;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.utils.item.ItemExamines;
import com.rs.world.item.Item;
import com.rs.world.item.ItemsContainer;
import com.rs.world.npc.familiar.Familiar;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Bank implements Serializable {

    public static final long MAX_BANK_SIZE = 506;
    /**
     *
     */
    private static final long serialVersionUID = 1551246756081236625L;
    private final ItemsContainer container = new ItemsContainer(95, true);
    public transient Item[] lastContainerCopy;
    // tab, items
    private Item[][] bankTabs;
    @SuppressWarnings("unused")
    private short bankPin;
    private int lastX;
    private transient Player player;
    private transient int currentTab;
    private transient boolean withdrawNotes;
    private transient boolean insertItems;

    public Bank() {
        bankTabs = new Item[1][0];
    }

    public void removeItem(final int id) {
        if (bankTabs != null) {
            for (final Item[] bankTab : bankTabs) {
                for (int i2 = 0; i2 < bankTab.length; i2++) {
                    if (bankTab[i2].getId() == id) {
                        bankTab[i2].setId(0); // dwarf remains
                    }
                }
            }
        }
    }

    public ItemsContainer getContainer() {
        return container;
    }

    public void setPlayer(final Player player) {
        this.player = player;
        if (bankTabs == null || bankTabs.length == 0) {
            bankTabs = new Item[1][0];
        }
    }

    public void refreshTabs() {
        for (int slot = 1; slot < 9; slot++) {
            refreshTab(slot);
        }
    }

    public int getTabSize(final int slot) {
        if (slot >= bankTabs.length)
            return 0;
        return bankTabs[slot].length;
    }

    public void withdrawLastAmount(final int bankSlot) {
        withdrawItem(bankSlot, lastX);
    }

    public void withdrawItemButOne(final int fakeSlot) {
        final int[] fromRealSlot = getRealSlot(fakeSlot);
        final Item item = getItem(fromRealSlot);
        if (item == null)
            return;
        if (item.getAmount() <= 1) {
            player.getPackets().sendGameMessage(
                    "You only have one of this item in your bank");
            return;
        }
        withdrawItem(fakeSlot, item.getAmount() - 1);
    }

    public void openPlayerBank(final Player victim) {
        if (victim == null)
            return;
        player.getInterfaceManager().sendInterface(762);
        player.getInterfaceManager().sendInventoryInterface(763);
        player.getPackets().sendItems(95, victim.getBank().getContainerCopy());
        refreshViewingTab();
        refreshTabs();
        unlockButtons();
    }

    public void depositLastAmount(final int bankSlot) {
        depositItem(bankSlot, lastX, true);
    }

    public void depositAllInventory(final boolean banking) {
        if (Bank.MAX_BANK_SIZE - getBankSize() < player.getInventory()
                .getItems().getSize()) {
            player.getPackets().sendGameMessage(
                    "Not enough space in your bank.");
            return;
        }
        for (int i = 0; i < 28; i++) {
            depositItem(i, Integer.MAX_VALUE, false);
        }
        refreshTab(currentTab);
        refreshItems();
    }

    private String getFormattedNumber(final int amount) {
        return new DecimalFormat("#,###,##0").format(amount);
    }

    public void depositMoneyPouch(final boolean banking) {
        if (player.getMoneyPouchValue() == 0)
            return;
        final int coinsCount = player.getMoneyPouchValue();
        final int space = addItems(new Item[]{new Item(995, coinsCount)},
                banking);
        if (space != 0) {
            if (Bank.MAX_BANK_SIZE - getBankSize() < player.getInventory()
                    .getItems().getSize()) {
                player.getPackets().sendGameMessage(
                        "Not enough space in your bank.");
                return;
            }
            player.getPackets().sendRunScript(5561, 0, coinsCount);
            player.getPackets().sendGameMessage(getFormattedNumber(player.getMoneyPouchValue()) + " coins have been removed from your money pouch.");
            player.setMoneyPouchValue(0);
            player.refreshMoneyPouch();
        }
    }

    public void depositAllBob(final boolean banking) {
        final Familiar familiar = player.getFamiliar();
        if (familiar == null || familiar.getBob() == null)
            return;
        final int space = addItems(
                familiar.getBob().getBeastItems().getItems(), banking);
        if (space != 0) {
            for (int i = 0; i < space; i++) {
                familiar.getBob().getBeastItems().set(i, null);
            }
            familiar.getBob().sendInterItems();
        }
        if (space < familiar.getBob().getBeastItems().getSize()) {
            player.getPackets().sendGameMessage(
                    "Not enough space in your bank.");
            return;
        }
    }

    public void depositAllEquipment(final boolean banking) {
        final int space = addItems(player.getEquipment().getItems().getItems(),
                banking);
        if (space != 0) {
            for (int i = 0; i < space; i++) {
                player.getEquipment().getItems().set(i, null);
            }
            player.getEquipment().init();
            player.getAppearance().generateAppearenceData();
        }
        if (space < player.getEquipment().getItems().getSize()) {
            player.getPackets().sendGameMessage(
                    "Not enough space in your bank.");
            return;
        }
    }

    public void collapse(final int tabId) {
        if (tabId == 0 || tabId >= bankTabs.length)
            return;
        final Item[] items = bankTabs[tabId];
        for (final Item item : items) {
            removeItem(getItemSlot(item.getId()), item.getAmount(), false, true);
        }
        for (final Item item : items) {
            addItem(item.getId(), item.getAmount(), 0, false);
        }
        refreshTabs();
        refreshItems();
    }

    public void switchItem(final int fromSlot, final int toSlot,
                           final int fromComponentId, final int toComponentId) {
        // System.out.println(fromSlot+", "+toSlot+", "+fromComponentId+", "+toComponentId);
        if (toSlot == 65535) {
            int toTab = toComponentId >= 76 ? 8 - (84 - toComponentId)
                    : 9 - ((toComponentId - 46) / 2);
            if (toTab < 0 || toTab > 9)
                return;
            if (bankTabs.length == toTab) {
                final int[] fromRealSlot = getRealSlot(fromSlot);
                if (fromRealSlot == null)
                    return;
                if (toTab == fromRealSlot[0]) {
                    switchItem(fromSlot, getStartSlot(toTab));
                    return;
                }
                final Item item = getItem(fromRealSlot);
                if (item == null)
                    return;
                removeItem(fromSlot, item.getAmount(), false, true);
                createTab();
                bankTabs[bankTabs.length - 1] = new Item[]{item};
                refreshTab(fromRealSlot[0]);
                refreshTab(toTab);
                refreshItems();
            } else if (bankTabs.length > toTab) {
                final int[] fromRealSlot = getRealSlot(fromSlot);
                if (fromRealSlot == null)
                    return;
                if (toTab == fromRealSlot[0]) {
                    switchItem(fromSlot, getStartSlot(toTab));
                    return;
                }
                final Item item = getItem(fromRealSlot);
                if (item == null)
                    return;
                final boolean removed = removeItem(fromSlot, item.getAmount(),
                        false, true);
                if (!removed) {
                    refreshTab(fromRealSlot[0]);
                } else if (fromRealSlot[0] != 0 && toTab >= fromRealSlot[0]) {
                    toTab -= 1;
                }
                refreshTab(fromRealSlot[0]);
                addItem(item.getId(), item.getAmount(), toTab, true);
            }
        } else {
            switchItem(fromSlot, toSlot);
        }
    }

    public void switchItem(final int fromSlot, final int toSlot) {
        final int[] fromRealSlot = getRealSlot(fromSlot);
        final Item fromItem = getItem(fromRealSlot);
        if (fromItem == null)
            return;
        final int[] toRealSlot = getRealSlot(toSlot);
        final Item toItem = getItem(toRealSlot);
        if (toItem == null)
            return;
        bankTabs[fromRealSlot[0]][fromRealSlot[1]] = toItem;
        bankTabs[toRealSlot[0]][toRealSlot[1]] = fromItem;
        refreshTab(fromRealSlot[0]);
        if (fromRealSlot[0] != toRealSlot[0]) {
            refreshTab(toRealSlot[0]);
        }
        refreshItems();
    }

    public void openSetPin() { // TODO

    }

    public void openDepositBox() {
        player.getInterfaceManager().sendInterface(11);
        player.getInterfaceManager().closeInventory();
        player.getInterfaceManager().closeEquipment();
        final int lastGameTab = player.getInterfaceManager().openGameTab(9); // friends
        // tab
        sendBoxInterItems();
        player.getPackets().sendIComponentText(11, 13,
                "Bank of " + Server.getInstance().getSettingsManager().getSettings().getServerName() + " - Deposit Box");
        player.setCloseInterfacesEvent(() -> {
            player.getInterfaceManager().sendInventory();
            player.getInventory().unlockInventoryOptions();
            player.getInterfaceManager().sendEquipment();
            player.getInterfaceManager().openGameTab(lastGameTab);
        });
    }

    public void sendBoxInterItems() {
        player.getPackets().sendInterSetItemsOptionsScript(11, 17, 93, 6, 5,
                "Deposit-1", "Deposit-5", "Deposit-10", "Deposit-All",
                "Deposit-X", "Examine");
        player.getPackets().sendUnlockIComponentOptionSlots(11, 17, 0, 27, 0,
                1, 2, 3, 4, 5);
    }

    public void openBank() {
        if (!player.isHasEnteredPin() && player.isHasBankPin()) {
            player.getTemporaryAttributtes().put("bank_pin1", Boolean.TRUE);
            player.getPackets().sendRunScript(108, "Enter Your Bank Pin Please");
        } else {
            GildedAltar.bonestoOffer.stopOfferGod = true;
            player.getInterfaceManager().sendInterface(762);
            player.getInterfaceManager().sendInventoryInterface(763);
            refreshViewingTab();
            refreshTabs();
            unlockButtons();
            sendItems();
            refreshLastX();
        }
    }

    public void refreshLastX() {
        player.getPackets().sendConfig(1249, lastX);
    }

    public void createTab() {
        final int slot = bankTabs.length;
        final Item[][] tabs = new Item[slot + 1][];
        System.arraycopy(bankTabs, 0, tabs, 0, slot);
        tabs[slot] = new Item[0];
        bankTabs = tabs;
    }

    public void destroyTab(final int slot) {
        final Item[][] tabs = new Item[bankTabs.length - 1][];
        System.arraycopy(bankTabs, 0, tabs, 0, slot);
        System.arraycopy(bankTabs, slot + 1, tabs, slot, bankTabs.length - slot
                - 1);
        bankTabs = tabs;
        if (currentTab != 0 && currentTab >= slot) {
            currentTab--;
        }
    }

    public void emptyBank() {
        for (Item i : getContainer().getItems()) {
            removeItem(i.getId());
        }
        refreshItems();
        Server.getInstance().getPlayerFileManager().save(player);
    }

    public boolean hasBankSpace() {
        return getBankSize() < MAX_BANK_SIZE;
    }

    public void withdrawItem(final int bankSlot, final int quantity) {
        if (quantity < 1)
            return;
        Item item = getItem(getRealSlot(bankSlot));
        if (item == null)
            return;
        if (item.getAmount() < quantity) {
            item = new Item(item.getId(), item.getAmount());
        } else {
            item = new Item(item.getId(), quantity);
        }
        boolean noted = false;
        final ItemDefinitions defs = item.getDefinitions();
        if (withdrawNotes) {
            if (!defs.isNoted() && defs.getCertId() != -1) {
                item.setId(defs.getCertId());
                noted = true;
            } else {
                player.getPackets().sendGameMessage(
                        "You cannot withdraw this item as a note.");
            }
        }
        if (noted || defs.isStackable()) {
            if (player.getInventory().getItems().containsOne(item)) {
                final int slot = player.getInventory().getItems()
                        .getThisItemSlot(item);
                final Item invItem = player.getInventory().getItems().get(slot);
                if (invItem.getAmount() + item.getAmount() <= 0) {
                    item.setAmount(Integer.MAX_VALUE - invItem.getAmount());
                    player.getPackets().sendGameMessage(
                            "Not enough space in your inventory.");
                }
            } else if (!player.getInventory().hasFreeSlots()) {
                player.getPackets().sendGameMessage(
                        "Not enough space in your inventory.");
                return;
            }
        } else {
            final int freeSlots = player.getInventory().getFreeSlots();
            if (freeSlots == 0) {
                player.getPackets().sendGameMessage(
                        "Not enough space in your inventory.");
                return;
            }
            if (freeSlots < item.getAmount()) {
                item.setAmount(freeSlots);
                player.getPackets().sendGameMessage(
                        "Not enough space in your inventory.");
            }
        }
        removeItem(bankSlot, item.getAmount(), true, false);
        player.getInventory().addItem(item);
    }

    public void sendExamine(final int fakeSlot) {
        final int[] slot = getRealSlot(fakeSlot);
        if (slot == null)
            return;
        final Item item = bankTabs[slot[0]][slot[1]];
        player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
    }

    public void depositItem(final int invSlot, final int quantity,
                            final boolean refresh) {
        if (quantity < 1 || invSlot < 0 || invSlot > 27)
            return;
        Item item = player.getInventory().getItem(invSlot);
        if (item == null)
            return;
        final int amt = player.getInventory().getItems().getNumberOf(item);
        if (amt < quantity) {
            item = new Item(item.getId(), amt);
        } else {
            item = new Item(item.getId(), quantity);
        }
        final ItemDefinitions defs = item.getDefinitions();
        final int originalId = item.getId();
        if (defs.isNoted() && defs.getCertId() != -1) {
            item.setId(defs.getCertId());
        }
        final Item bankedItem = getItem(item.getId());
        if (bankedItem != null) {
            if (bankedItem.getAmount() + item.getAmount() <= 0) {
                item.setAmount(Integer.MAX_VALUE - bankedItem.getAmount());
                player.getPackets().sendGameMessage(
                        "Not enough space in your bank.");
            }
        } else if (!hasBankSpace()) {
            player.getPackets().sendGameMessage(
                    "Not enough space in your bank.");
            return;
        }
        player.getInventory().deleteItem(invSlot,
                new Item(originalId, item.getAmount()));
        addItem(item, refresh);
    }

    private void addItem(final Item item, final boolean refresh) {
        addItem(item.getId(), item.getAmount(), refresh);
    }

    public int addItems(final Item[] items, final boolean refresh) {
        int space = (int) (MAX_BANK_SIZE - getBankSize());
        if (space != 0) {
            space = (space < items.length ? space : items.length);
            for (int i = 0; i < space; i++) {
                if (items[i] == null) {
                    continue;
                }
                addItem(items[i], false);
            }
            if (refresh) {
                refreshTabs();
                refreshItems();
            }
        }
        return space;
    }

    public void addItem(final int id, final int quantity, final boolean refresh) {
        addItem(id, quantity, currentTab, refresh);
    }

    public void addItem(final int id, final int quantity, int creationTab,
                        final boolean refresh) {
        final int[] slotInfo = getItemSlot(id);
        if (slotInfo == null) {
            if (creationTab >= bankTabs.length) {
                creationTab = bankTabs.length - 1;
            }
            if (creationTab < 0) {
                creationTab = 0;
            }
            final int slot = bankTabs[creationTab].length;
            final Item[] tab = new Item[slot + 1];
            System.arraycopy(bankTabs[creationTab], 0, tab, 0, slot);
            tab[slot] = new Item(id, quantity);
            bankTabs[creationTab] = tab;
            if (refresh) {
                refreshTab(creationTab);
            }
        } else {
            final Item item = bankTabs[slotInfo[0]][slotInfo[1]];
            bankTabs[slotInfo[0]][slotInfo[1]] = new Item(item.getId(),
                    item.getAmount() + quantity);
        }
        if (refresh) {
            refreshItems();
        }
    }

    public boolean removeItem(final int fakeSlot, final int quantity,
                              final boolean refresh, final boolean forceDestroy) {
        return removeItem(getRealSlot(fakeSlot), quantity, refresh,
                forceDestroy);
    }

    public boolean removeItem(final int[] slot, final int quantity,
                              final boolean refresh, final boolean forceDestroy) {
        if (slot == null)
            return false;
        final Item item = bankTabs[slot[0]][slot[1]];
        boolean destroyed = false;
        if (quantity >= item.getAmount()) {
            if (bankTabs[slot[0]].length == 1
                    && (forceDestroy || bankTabs.length != 1)) {
                destroyTab(slot[0]);
                if (refresh) {
                    refreshTabs();
                }
                destroyed = true;
            } else {
                final Item[] tab = new Item[bankTabs[slot[0]].length - 1];
                System.arraycopy(bankTabs[slot[0]], 0, tab, 0, slot[1]);
                System.arraycopy(bankTabs[slot[0]], slot[1] + 1, tab, slot[1],
                        bankTabs[slot[0]].length - slot[1] - 1);
                bankTabs[slot[0]] = tab;
                if (refresh) {
                    refreshTab(slot[0]);
                }
            }
        } else {
            bankTabs[slot[0]][slot[1]] = new Item(item.getId(),
                    item.getAmount() - quantity);
        }
        if (refresh) {
            refreshItems();
        }
        return destroyed;
    }

    public Item getItem(final int id) {
        for (final Item[] bankTab : bankTabs) {
            for (final Item item : bankTab)
                if (item.getId() == id)
                    return item;
        }
        return null;
    }

    public int[] getItemSlot(final int id) {
        for (int tab = 0; tab < bankTabs.length; tab++) {
            for (int slot = 0; slot < bankTabs[tab].length; slot++)
                if (bankTabs[tab][slot].getId() == id)
                    return new int[]{tab, slot};
        }
        return null;
    }

    public Item getItem(final int[] slot) {
        if (slot == null)
            return null;
        return bankTabs[slot[0]][slot[1]];
    }

    public int getStartSlot(final int tabId) {
        int slotId = 0;
        for (int tab = 1; tab < (tabId == 0 ? bankTabs.length : tabId); tab++) {
            slotId += bankTabs[tab].length;
        }

        return slotId;

    }

    public int[] getRealSlot(int slot) {
        for (int tab = 1; tab < bankTabs.length; tab++) {
            if (slot >= bankTabs[tab].length) {
                slot -= bankTabs[tab].length;
            } else
                return new int[]{tab, slot};
        }
        if (slot >= bankTabs[0].length)
            return null;
        return new int[]{0, slot};
    }

    public void refreshViewingTab() {
        player.getPackets().sendConfigByFile(4893, currentTab + 1);
    }

    public void refreshTab(final int slot) {
        if (slot == 0)
            return;
        player.getPackets().sendConfigByFile(4885 + (slot - 1),
                getTabSize(slot));
    }

    public void sendItems() {
        player.getPackets().sendItems(95, getContainerCopy());
    }

    public void refreshItems(final int[] slots) {
        player.getPackets().sendUpdateItems(95, getContainerCopy(), slots);
    }

    public Item[] getContainerCopy() {
        if (lastContainerCopy == null) {
            lastContainerCopy = generateContainer();
        }
        return lastContainerCopy;
    }

    public void refreshItems() {
        refreshItems(generateContainer(), getContainerCopy());
    }

    public void refreshItems(final Item[] itemsAfter, final Item[] itemsBefore) {
        if (itemsBefore.length != itemsAfter.length) {
            lastContainerCopy = itemsAfter;
            sendItems();
            return;
        }
        final int[] changedSlots = new int[itemsAfter.length];
        int count = 0;
        for (int index = 0; index < itemsAfter.length; index++) {
            if (itemsBefore[index] != itemsAfter[index]) {
                changedSlots[count++] = index;
            }
        }
        final int[] finalChangedSlots = new int[count];
        System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
        lastContainerCopy = itemsAfter;
        refreshItems(finalChangedSlots);
    }

    public int getBankSize() {
        int size = 0;
        for (final Item[] bankTab : bankTabs) {
            size += bankTab.length;
        }
        return size;
    }

    public Item[] generateContainer() {
        final Item[] container = new Item[getBankSize()];
        int count = 0;
        for (int slot = 1; slot < bankTabs.length; slot++) {
            System.arraycopy(bankTabs[slot], 0, container, count,
                    bankTabs[slot].length);
            count += bankTabs[slot].length;
        }
        System.arraycopy(bankTabs[0], 0, container, count, bankTabs[0].length);
        return container;
    }

    public void unlockButtons() {
        // unlock bank inter all options
        player.getPackets().sendIComponentSettings(762, 95, 0, 516, 2622718);
        // unlock bank inv all options
        player.getPackets().sendIComponentSettings(763, 0, 0, 27, 2425982);
    }

    public void switchWithdrawNotes() {
        withdrawNotes = !withdrawNotes;
    }

    public void switchInsertItems() {
        insertItems = !insertItems;
        player.getPackets().sendConfig(305, insertItems ? 1 : 0);
    }

    public void setCurrentTab(final int currentTab) {
        if (currentTab >= bankTabs.length)
            return;
        this.currentTab = currentTab;
    }

    public int getLastX() {
        return lastX;
    }

    public void setLastX(final int lastX) {
        this.lastX = lastX;
    }
}
