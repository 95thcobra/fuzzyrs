package com.rs.player.content;

import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.content.dialogues.types.SimpleNPCMessage;
import com.rs.player.Player;
import com.rs.world.item.Item;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class RepairItems {

	public static void Repair(final Player player, final int itemId,
							  final int amount) {
		final BrokenItems brokenitems = BrokenItems.forId(itemId);
		final Item item = new Item(brokenitems.getId(), 1);
		final int price = brokenitems.getPrice();
		if (amount == 1) {
			if (player.getInventory().containsItem(995, price)) {
				player.getInventory().deleteItem(itemId, 1);
				player.getInventory().deleteItem(995, price);
				player.getInventory().addItem(brokenitems.getId2(), 1);
				player.getDialogueManager().startDialogue(
						SimpleMessage.class,
						"You have repaired your item(" + item.getName()
								+ ") for " + getFormattedNumber(price)
								+ " coins.");
				return;
			} else {
				player.getDialogueManager().startDialogue(
						SimpleMessage.class,
						"You dont have enough money to repair this item."
								+ "You need " + getFormattedNumber(price)
								+ " coins.");
				return;
			}
		} else {
			if (player.getInventory().containsItem(995, price * amount)) {
				player.getInventory().deleteItem(itemId, amount);
				player.getInventory().deleteItem(995, price * amount);
				player.getInventory().addItem(brokenitems.getId2(), amount);
				player.getDialogueManager().startDialogue(
						SimpleMessage.class,
						"You have repaired your items(" + amount + " X "
								+ item.getName() + ") for "
								+ getFormattedNumber(price * amount)
								+ " coins.");
				return;
			} else {
				player.getDialogueManager().startDialogue(
						SimpleMessage.class,
						"You dont have enough money to repair these items."
								+ "You need "
								+ getFormattedNumber(price * amount)
								+ " coins.");
				return;
			}
		}
	}

	public static void CheckPrice(final Player player, final int itemId,
								  final int amount) {
		final BrokenItems brokenitems = BrokenItems.forId(itemId);
		final int price = brokenitems.getPrice();
		player.getDialogueManager().startDialogue(
				SimpleNPCMessage.class,
				945,
				"These items will cost you "
						+ getFormattedNumber(price * amount) + " coins.");
		return;
	}

	private static String getFormattedNumber(final int amount) {
		return new DecimalFormat("#,###,##0").format(amount).toString();
	}

	public enum BrokenItems {
		// Main structure NAME(Broken itemId, repaired itemId,Price to repair
		// it)
		GUTHANS_HELM(4908, 4724, 100000),

		GUTHANS_PLATEBODY(4920, 4738, 100000),

		GUTHANS_PLATESKIRT(4926, 4730, 100000),

		GUTHANS_WARSPEAR(4914, 4726, 100000),

		AHRIMS_HOOD(4860, 4708, 100000),

		AHRIMS_STAFF(4866, 4710, 100000),

		AHRIMS_ROBE_TOP(4872, 4712, 100000),

		AHRIMS_ROBE_SKIRT(4878, 4714, 100000),

		DHAROKS_HELM(4884, 4716, 100000),

		DHAROKS_GREATAXE(4890, 4718, 100000),

		DHAROKS_PLATEBODY(4896, 4720, 100000),

		DHAROKS_PLATELEGS(4902, 4722, 100000),

		KARILS_COIF(4932, 4732, 100000),

		KARILS_CROSSBOW(4938, 4734, 100000),

		KARILS_TOP(4944, 4736, 100000),

		KARILS_SKIRT(4950, 4738, 100000),

		TORAGS_HELM(4956, 4745, 100000),

		TORAGS_HAMMER(4962, 4747, 100000),

		TORAGS_PLATEBODY(4968, 4749, 100000),

		TORAGS_PLATELEGS(4974, 4751, 100000),

		VERACS_HELM(4980, 4753, 100000),

		VERACS_FLAIL(4968, 4755, 100000),

		VERACS_BRASSARD(4992, 4757, 100000),

		VERACS_PLATESKIRT(4998, 4759, 100000),

		TORVA_HELM(20138, 20135, 2000000),

		TORVA_PLATE(20142, 20139, 2000000),

		TORVA_LEGS(20146, 20143, 2000000),

		TORVA_GLOVES(24979, 24977, 2000000),

		TORVA_BOOTS(24985, 24983, 2000000),

		PERNIX_COWL(20150, 20147, 1500000),

		PERNIX_BODY(20154, 20151, 1500000),

		PERNIX_CHAPS(20158, 20155, 1500000),

		PERNIX_GLOVES(24976, 24974, 1500000),

		PERNIX_BOOTS(24991, 24989, 1500000),

		ZARYTE_BOW(20174, 20171, 2000000),

		VIRTUS_MASK(20162, 20159, 1000000),

		VIRTUS_TOP(20166, 20163, 1000000),

		VIRTUS_LEGS(20170, 20167, 1000000),

		VIRTUS_GLOVES(24982, 24980, 1000000),

		VIRTUS_BOOTS(24988, 24986, 1000000);

		private static Map<Integer, BrokenItems> BROKENITEMS = new HashMap<Integer, BrokenItems>();

		static {
			for (final BrokenItems brokenitems : BrokenItems.values()) {
				BROKENITEMS.put(brokenitems.getId(), brokenitems);
			}
		}

		private final int id;
		private final int id2;
		private final int Price;

		BrokenItems(final int id, final int id2, final int Price) {
			this.id = id;
			this.id2 = id2;
			this.Price = Price;
		}

		public static BrokenItems forId(final int id) {
			return BROKENITEMS.get(id);
		}

		public int getId() {
			return id;
		}

		public int getId2() {
			return id2;
		}

		public int getPrice() {
			return Price;
		}

	}

}