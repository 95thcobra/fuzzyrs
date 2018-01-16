package com.rs.player;

import com.rs.core.settings.GameConstants;
import com.rs.core.settings.SettingsManager;
import com.rs.core.utils.file.EconomyPrices;
import com.rs.core.utils.item.ItemExamines;
import com.rs.world.item.Item;
import com.rs.world.item.ItemConstants;
import com.rs.world.item.ItemsContainer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Trade {

	private final Player player;
	private final ItemsContainer<Item> items;
	private Player target;
	private boolean tradeModified;
	private boolean accepted;

	public Trade(final Player player) {
		this.player = player; // player reference
		items = new ItemsContainer<>(28, false);
	}

	public void addMoneyPouch(int value) {
		Item[] itemsBefore = items.getItemsCopy();
		Item item = new Item(995, value);
		items.add(item);
		refreshItems(itemsBefore);
		cancelAccepted();
		player.setAddedFromPouch(true);
	}

	/*
	 * called to both players
	 */
	public void openTrade(final Player target) {
		synchronized (this) {
			synchronized (target.getTrade()) {
				int total = 0;
				final int reqTotal = 400;
				for (int i = 0; i < 25; i++) {
					total += player.getSkills().getLevel(i);
				}
				if (total < reqTotal) {
					player.sendMessage("You must have a total level of 400 to trade!");
					return;
				}
				/*if (player.ironmode == true && target.ironmode == false) {
					player.sendMessage("You can only trade with other Iron men.");
					return;
				}
				if (player.ironmode == false && target.ironmode == true) {
					player.sendMessage("The other player can only trade with other Iron men.");
					return;
				}*/
				this.target = target;
				player.getPackets().sendIComponentText(335, 17,
						"Trading With: " + target.getDisplayName());
				player.getPackets().sendGlobalString(203,
						target.getDisplayName());
				sendInterItems();
				sendOptions();
				sendTradeModified();
				refreshFreeInventorySlots();
				refreshTradeWealth();
				refreshStageMessage(true);
				player.getInterfaceManager().sendInterface(335);
				player.getInterfaceManager().sendInventoryInterface(336);
				player.setCloseInterfacesEvent(new Runnable() {
					@Override
					public void run() {
						closeTrade(CloseTradeStage.CANCEL);
					}
				});
			}
		}
	}

	public void removeItem(final int slot, final int amount) {
		synchronized (this) {
			if (!isTrading())
				return;
			synchronized (target.getTrade()) {
				Item item = items.get(slot);
				if (item == null)
					return;
				final Item[] itemsBefore = items.getItemsCopy();
				final int maxAmount = items.getNumberOf(item);
				if (amount < maxAmount) {
					item = new Item(item.getId(), amount);
				} else {
					item = new Item(item.getId(), maxAmount);
				}
				items.remove(slot, item);
				player.getInventory().addItem(item);
				refreshItems(itemsBefore);
				cancelAccepted();
				setTradeModified(true);
			}
		}
	}

	public void sendFlash(final int slot) {
		player.getPackets().sendInterFlashScript(335, 33, 4, 7, slot);
		target.getPackets().sendInterFlashScript(335, 36, 4, 7, slot);
	}

	public void cancelAccepted() {
		boolean canceled = false;
		if (accepted) {
			accepted = false;
			canceled = true;
		}
		if (target.getTrade().accepted) {
			target.getTrade().accepted = false;
			canceled = true;
		}
		if (canceled) {
			refreshBothStageMessage(canceled);
		}
	}

	public void addItem(final int slot, final int amount) {
		synchronized (this) {
			if (!isTrading())
				return;
			synchronized (target.getTrade()) {
				Item item = player.getInventory().getItem(slot);
				if (item == null)
					return;
				if (!ItemConstants.isTradeable(item)
						&& (!player.getUsername().equalsIgnoreCase(SettingsManager.getSettings().OWNERS[0]))) {
					player.getPackets()
							.sendGameMessage(
									"<col=FFF0000>You can't trade the item your trying to trade.");
					return;
				}
				final Item[] itemsBefore = items.getItemsCopy();
				final int maxAmount = player.getInventory().getItems()
						.getNumberOf(item);
				if (amount < maxAmount) {
					item = new Item(item.getId(), amount);
				} else {
					item = new Item(item.getId(), maxAmount);
				}
				items.add(item);
				player.getInventory().deleteItem(slot, item);
				refreshItems(itemsBefore);
				cancelAccepted();
			}
		}
	}

	public void refreshItems(final Item[] itemsBefore) {
		final int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			final Item item = items.getItems()[index];
			if (itemsBefore[index] != item) {
				if (itemsBefore[index] != null
						&& (item == null
								|| item.getId() != itemsBefore[index].getId() || item
								.getAmount() < itemsBefore[index].getAmount())) {
					sendFlash(index);
				}
				changedSlots[count++] = index;
			}
		}
		final int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
		refreshFreeInventorySlots();
		refreshTradeWealth();
	}

	public void sendOptions() {
		player.getPackets().sendInterSetItemsOptionsScript(336, 0, 93, 4, 7,
				"Offer", "Offer-5", "Offer-10", "Offer-All", "Offer-X",
				"Value<col=FF9040>", "Lend");
		player.getPackets().sendIComponentSettings(336, 0, 0, 27, 1278);
		player.getPackets().sendInterSetItemsOptionsScript(335, 32, 90, 4, 7,
				"Remove", "Remove-5", "Remove-10", "Remove-All", "Remove-X",
				"Value");
		player.getPackets().sendIComponentSettings(335, 32, 0, 27, 1150);
		player.getPackets().sendInterSetItemsOptionsScript(335, 35, 90, true,
				4, 7, "Value");
		player.getPackets().sendIComponentSettings(335, 35, 0, 27, 1026);

	}

	public boolean isTrading() {
		return target != null;
	}

	public void setTradeModified(final boolean modified) {
		if (modified == tradeModified)
			return;
		tradeModified = modified;
		sendTradeModified();
	}

	public void sendInterItems() {
		player.getPackets().sendItems(90, items);
		target.getPackets().sendItems(90, true, items);
	}

	public void refresh(final int... slots) {
		player.getPackets().sendUpdateItems(90, items, slots);
		target.getPackets().sendUpdateItems(90, true, items.getItems(), slots);
	}

	public void accept(final boolean firstStage) {
		synchronized (this) {
			if (!isTrading())
				return;
			synchronized (target.getTrade()) {
				if (target.getTrade().accepted) {
					if (firstStage) {
						if (nextStage()) {
							target.getTrade().nextStage();
						}
					} else {
						player.setCloseInterfacesEvent(null);
						player.closeInterfaces();
						closeTrade(CloseTradeStage.DONE);
					}
					return;
				}
				accepted = true;
				refreshBothStageMessage(firstStage);
			}
		}
	}

	public void sendValue(final int slot, final boolean traders) {
		if (!isTrading())
			return;
		final Item item = traders ? target.getTrade().items.get(slot) : items
				.get(slot);
		if (item == null)
			return;
		if (!ItemConstants.isTradeable(item)) {
			player.getPackets().sendGameMessage("That item isn't tradeable.");
			return;
		}
		final int price = EconomyPrices.getPrice(item.getId());
		player.getPackets().sendGameMessage(
				item.getDefinitions().getName() + ": market price is " + price
						+ " coins.");
	}

	public void sendValue(final int slot) {
		final Item item = player.getInventory().getItem(slot);
		if (item == null)
			return;
		if (!ItemConstants.isTradeable(item)) {
			player.getPackets().sendGameMessage("That item isn't tradeable.");
			return;
		}
		final int price = EconomyPrices.getPrice(item.getId());
		player.getPackets().sendGameMessage(
				item.getDefinitions().getName() + ": market price is " + price
						+ " coins.");
	}

	public void sendExamine(final int slot, final boolean traders) {
		if (!isTrading())
			return;
		final Item item = traders ? target.getTrade().items.get(slot) : items
				.get(slot);
		if (item == null)
			return;
		player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	public boolean nextStage() {
		if (!isTrading())
			return false;
		if (player.getInventory().getItems().getUsedSlots()
				+ target.getTrade().items.getUsedSlots() > 28) {
			player.setCloseInterfacesEvent(null);
			player.closeInterfaces();
			closeTrade(CloseTradeStage.NO_SPACE);
			return false;
		}
		accepted = false;
		player.getInterfaceManager().sendInterface(334);
		player.getInterfaceManager().closeInventoryInterface();
		player.getPackets().sendHideIComponent(334, 55,
				!(tradeModified || target.getTrade().tradeModified));
		refreshBothStageMessage(false);
		return true;
	}

	public void refreshBothStageMessage(final boolean firstStage) {
		refreshStageMessage(firstStage);
		target.getTrade().refreshStageMessage(firstStage);
	}

	public void refreshStageMessage(final boolean firstStage) {
		player.getPackets().sendIComponentText(firstStage ? 335 : 334,
				firstStage ? 39 : 34, getAcceptMessage(firstStage));
	}

	public String getAcceptMessage(final boolean firstStage) {
		if (accepted)
			return "Waiting for other player...";
		if (target.getTrade().accepted)
			return "Other player has accepted.";
		return firstStage ? "" : "Are you sure you want to make this trade?";
	}

	public void sendTradeModified() {
		player.getPackets().sendConfig(1042, tradeModified ? 1 : 0);
		target.getPackets().sendConfig(1043, tradeModified ? 1 : 0);
	}

	public void refreshTradeWealth() {
		final int wealth = getTradeWealth();
		player.getPackets().sendGlobalConfig(729, wealth);
		target.getPackets().sendGlobalConfig(697, wealth);
	}

	public void refreshFreeInventorySlots() {
		final int freeSlots = player.getInventory().getFreeSlots();
		target.getPackets().sendIComponentText(
				335,
				23,
				"has " + (freeSlots == 0 ? "no" : freeSlots) + " free"
						+ "<br>inventory slots");
	}

	public int getTradeWealth() {
		int wealth = 0;
		for (final Item item : items.getItems()) {
			if (item == null) {
				continue;
			}
			wealth += EconomyPrices.getPrice(item.getId()) * item.getAmount();
		}
		return wealth;
	}

	public void logThis(final String lastMsg) {
	}

	public void closeTrade(final CloseTradeStage stage) {
		synchronized (this) {
			synchronized (target.getTrade()) {
				final Player oldTarget = target;
				target = null;
				tradeModified = false;
				accepted = false;
				if (CloseTradeStage.DONE != stage) {
					player.getInventory().getItems().addAll(items);
					player.getInventory().init();
					items.clear();
				} else {
					try {
						final DateFormat dateFormat = new SimpleDateFormat(
								"MM/dd/yy HH:mm:ss");
						final Calendar cal = Calendar.getInstance();
						System.out.println(dateFormat.format(cal.getTime()));
						final String FILE_PATH = GameConstants.DATA_PATH + "/playersaves/logs/tradelogs/";
						final BufferedWriter writer = new BufferedWriter(
								new FileWriter(FILE_PATH
										+ oldTarget.getUsername() + ".txt",
										true));
						writer.write("[" + dateFormat.format(cal.getTime())
								+ "] : " + items);
						writer.newLine();
						writer.flush();
						writer.close();
					} catch (final IOException er) {
						System.out.println("Error logging Recived trade.");
					}
					player.getPackets().sendGameMessage("Accepted trade.");
					player.getInventory().getItems()
							.addAll(oldTarget.getTrade().items);
					player.getInventory().init();
					oldTarget.getTrade().items.clear();
				}
				if (oldTarget.getInventory().getItems()
						.goesOverAmount(oldTarget.getTrade().items)) {
					oldTarget.getPackets().sendGameMessage(
							"You'll go over max of an item!");
					player.getPackets().sendGameMessage(
							"They'll go over max of an item!");
					oldTarget.setCloseInterfacesEvent(null);
					oldTarget.closeInterfaces();
					oldTarget.getTrade().closeTrade(stage);
					return;
				}
				if (player.getInventory().getItems().goesOverAmount(items)) {
					player.getPackets().sendGameMessage(
							"You'll go over max of an item!");
					oldTarget.getPackets().sendGameMessage(
							"They'll go over max of an item!");
					player.setCloseInterfacesEvent(null);
					player.closeInterfaces();
					player.getTrade().closeTrade(stage);
					return;
				}
				if (oldTarget.getTrade().isTrading()) {
					oldTarget.setCloseInterfacesEvent(null);
					oldTarget.closeInterfaces();
					oldTarget.getTrade().closeTrade(stage);
					if (CloseTradeStage.CANCEL == stage) {
						oldTarget.getPackets().sendGameMessage(
								"<col=ff0000>Other player declined trade!");
					} else if (CloseTradeStage.NO_SPACE == stage) {
						player.getPackets()
								.sendGameMessage(
										"You don't have enough space in your inventory for this trade.");
						oldTarget
								.getPackets()
								.sendGameMessage(
										"Other player doesn't have enough space in their inventory for this trade.");
					}
				}
			}
		}
	}

	private enum CloseTradeStage {
		CANCEL, NO_SPACE, DONE
	}

}
