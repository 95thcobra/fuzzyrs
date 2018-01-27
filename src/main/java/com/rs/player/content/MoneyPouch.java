package com.rs.player.content;

import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;

import java.text.DecimalFormat;

public class MoneyPouch {

	private Player player;

	public MoneyPouch(Player player) {
		this.player = player;
	}

	public void addMoneyFromInventory(int amount, boolean delete) {
		if (player.getInventory().getNumberOf(995) > Integer.MAX_VALUE
				- getTotal()) {
			amount = Integer.MAX_VALUE - getTotal();
		}
		if (getTotal() == Integer.MAX_VALUE) {
			player.getPackets().sendGameMessage("You can't store more in your money pouch.");
			return;
		}
		if (amount > 1) {
			player.getPackets().sendGameMessage(
					Utils.getFormattedNumber(amount, ',')
							+ " coins have been added to your money pouch.");
		} else {
			player.getPackets().sendGameMessage(
					"One coin has been added to your money pouch.");
		}
		player.getPackets().sendRunScript(5561, 1, amount);
		player.getMoneyPouch().setTotal(
				player.getMoneyPouch().getTotal() + amount);
		if (delete) {
			player.getInventory().deleteItem(new Item(995, amount));
		}
		refresh();
	}

	public void addMoney(int amount, boolean delete) {
		// if (!player.getControllerManager().processMoneyPouch())
		// return;
		if (delete) {
			if (player.getInventory().getNumberOf(995) > Integer.MAX_VALUE
					- getTotal()) {
				amount = Integer.MAX_VALUE - getTotal();
			}
		}
		int leftOver = 0;
		int inventoryLeftOver = 0;
		if (getTotal() + amount > Integer.MAX_VALUE || getTotal() + amount < 0) {
			if (getTotal() != Integer.MAX_VALUE)
			player.getPackets()
					.sendGameMessage(
							"Your money pouch is not big enough to hold that much cash.");
			leftOver = Integer.MAX_VALUE - getTotal();
			amount = amount - leftOver;
			if (getTotal() != Integer.MAX_VALUE) {
				player.getPackets().sendRunScript(5561, 1, leftOver);
				player.getMoneyPouch().setTotal(Integer.MAX_VALUE);
				refresh();
			}
			if (player.getInventory().getNumberOf(995) + amount > Integer.MAX_VALUE
					|| player.getInventory().getNumberOf(995) + amount < 0) {
				inventoryLeftOver = Integer.MAX_VALUE - player.getInventory().getNumberOf(995);
				amount = amount - inventoryLeftOver;
				if (player.getInventory().getNumberOf(995) != Integer.MAX_VALUE)
				player.getInventory().addItem(995, inventoryLeftOver);
				if (delete) {
					World.addGroundItem(new Item(995, amount), player, player,
							true, 60);
					player.getInventory().deleteItem(995, amount);
				} else {
					World.updateGroundItem(new Item(995, amount),
							new WorldTile(player), player, 60);
				}
				return;
			} else {
				if (delete) {
					player.getInventory().addItem(new Item(995, amount));
					player.getInventory().deleteItem(995, amount);
				} else
					player.getInventory().addItem(new Item(995, amount));
				return;
			}
		}
		if (getTotal() == Integer.MAX_VALUE) {
			return;
		}
		if (amount > 1) {
			player.getPackets().sendGameMessage(
					Utils.getFormattedNumber(amount, ',')
							+ " coins have been added to your money pouch.");
		} else {
			player.getPackets().sendGameMessage(
					"One coin has been added to your money pouch.");
		}
		player.getPackets().sendRunScript(5561, 1, amount);
		player.getMoneyPouch().setTotal(
				player.getMoneyPouch().getTotal() + amount);
		if (delete) {
			player.getInventory().deleteItem(new Item(995, amount));
		}
		refresh();
	}

	public void addMoneyMisc(int amount) {
		if (getTotal() + amount > Integer.MAX_VALUE || getTotal() + amount < 0) {
			player.getPackets()
					.sendGameMessage(
							"Your money pouch is not big enough to hold that much cash.");
			World.addGroundItem(new Item(995, amount), player, player, true, 60);
			return;
		}
		if (amount > 1) {
			player.getPackets().sendGameMessage(
					Utils.getFormattedNumber(amount, ',')
							+ " coins have been added to your money pouch.");
		} else {
			player.getPackets().sendGameMessage(
					"One coin has been added to your money pouch.");
		}
		player.getPackets().sendRunScript(5561, 1, amount);
		player.getMoneyPouch().setTotal(
				player.getMoneyPouch().getTotal() + amount);
		refresh();
	}

	public void addOverFlowMoney(int amount) {
		if (getTotal() + amount > Integer.MAX_VALUE || getTotal() + amount < 0) {
			player.getPackets()
					.sendGameMessage(
							"Your money pouch is not big enough to hold that much cash.");
			World.addGroundItem(new Item(995, amount), player, player, true, 60);
			return;
		}
		if (getTotal() != Integer.MAX_VALUE) {
			if (amount + getTotal() < 0) {
				amount = Integer.MAX_VALUE - getTotal();
			}
			if (amount > 1) {
				player.getPackets()
						.sendGameMessage(
								Utils.getFormattedNumber(amount, ',')
										+ " coins have been added to your money pouch.");
			} else {
				player.getPackets().sendGameMessage(
						"One coin has been added to your money pouch.");
			}
			player.getPackets().sendRunScript(5561, 1, amount);
			player.getMoneyPouch().setTotal(
					player.getMoneyPouch().getTotal() + amount);
			refresh();
		} else {
			player.getPackets().sendGameMessage("Money pouch overflow.");
		}
	}

	private String getFormattedNumber(int amount) {
		return new DecimalFormat("#,###,##0").format(amount);
	}

	public int getTotal() {
		return player.getMoneyPouchValue();
	}

	public void setTotal(int amount) {
		player.setMoneyPouchValue(amount);
	}

	public void refresh() {
		player.getPackets().sendRunScript(5560, player.getMoneyPouchValue());
	}

	public boolean removeMoneyMisc(int amount) {
		if (amount <= 0 || getTotal() == 0) {
			return false;
		}
		if (getTotal() < amount) {
			amount = getTotal();
		}
		player.getPackets().sendGameMessage(
				Utils.getFormattedNumber(amount, ',')
						+ " coins have been removed from your money pouch.");
		player.getMoneyPouch().setTotal(
				player.getMoneyPouch().getTotal() - amount);
		player.getPackets().sendRunScript(5561, 0, amount);
		refresh();
		return true;
	}

	public boolean takeMoneyFromPouch(int amount) {
		if (getTotal() - amount < 0)
			amount = getTotal();
		if (amount == 0)
			return false;
		setTotal(getTotal() - amount);
		player.getPackets().sendRunScript(5561, 0, amount);
		player.getPackets().sendGameMessage(
				(amount == 1 ? "One" : Utils.getFormattedNumber(amount, ','))
						+ " coin"
						+ (amount == 1 ? "" : "s")
						+ " "
						+ (amount == 1 ? "has" : "have")
						+ " been withdrawn to your money pouch.");
		refresh();
		return true;
	}

	public void sendExamine() {
		player.getPackets().sendGameMessage(
				"Your money pouch current contains "
						+ Utils.getFormattedNumber(player.getMoneyPouchValue(), ',')
						+ " coins.");
	}

	public void withdrawPouch(int amount) {
		// if (!player.getControllerManager().processMoneyPouch())
		// return;
		if (player.getInventory().getNumberOf(995) == Integer.MAX_VALUE
				|| amount <= 0 || getTotal() <= 0
				|| player.getInventory().getFreeSlots() == 0) {
			player.getPackets().sendGameMessage(
					"Not enough space in inventory.");
			return;
		}
		if (player.getInventory().getNumberOf(995) > Integer.MAX_VALUE - amount) {
			amount = Integer.MAX_VALUE - player.getInventory().getNumberOf(995);
		}
		if (amount > getTotal()) {
			amount = getTotal();
		}
		if (amount > 1) {
			player.getPackets()
					.sendGameMessage(
							Utils.getFormattedNumber(amount, ',')
									+ " coins have been removed from your money pouch.");
		} else {
			player.getPackets().sendGameMessage(
					"One coin has been removed from your money pouch.");
		}
		player.getMoneyPouch().setTotal(
				player.getMoneyPouch().getTotal() - amount);
		player.getInventory().addItem(new Item(995, amount));
		player.getPackets().sendRunScript(5561, 0, amount);
		refresh();
	}

	public boolean cantAdd() {
		return getTotal() == Integer.MAX_VALUE;
	}
}