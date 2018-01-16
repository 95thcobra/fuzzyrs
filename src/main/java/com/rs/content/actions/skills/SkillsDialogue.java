package com.rs.content.actions.skills;

import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.player.Player;

public final class SkillsDialogue {

	public static final int MAKE = 0, MAKE_SETS = 1, COOK = 2, ROAST = 3,
			OFFER = 4, SELL = 5, BAKE = 6, CUT = 7, DEPOSIT = 8,
			MAKE_NO_ALL_NO_CUSTOM = 9, TELEPORT = 10, SELECT = 11, TAKE = 13;
    private static int[] items;

    private SkillsDialogue() {

    }

    public static void setItem(int... item) {
        items = item;
    }

    public static int getItem(int slot) {
        return items[slot];
    }

	public static void sendSkillsDialogue(final Player player,
			final int option, final String explanation, final int maxQuantity,
			final int[] items, final ItemNameFilter filter) {
		sendSkillsDialogue(player, option, explanation, maxQuantity, items,
				filter, true);
	}

	public static void sendSkillsDialogue(final Player player,
			final int option, final String explanation, int maxQuantity,
			final int[] items, final ItemNameFilter filter,
			final boolean sendQuantitySelector) {
		player.getInterfaceManager().sendChatBoxInterface(905);
		player.getPackets().sendInterface(true, 905, 4, 916);
		if (!sendQuantitySelector) {
			maxQuantity = -1;
			player.getPackets().sendHideIComponent(916, 4, true);
			player.getPackets().sendHideIComponent(916, 9, true);
		} else {
			if (option != MAKE_SETS && option != MAKE_NO_ALL_NO_CUSTOM) {
				player.getPackets().sendUnlockIComponentOptionSlots(916, 8, -1,
						0, 0); // unlocks all option
			}
		}
		player.getPackets().sendIComponentText(916, 1, explanation);
		player.getPackets().sendGlobalConfig(754, option);
		for (int i = 0; i < 10; i++) {
			if (i >= items.length) {
				player.getPackets().sendGlobalConfig(
						i >= 6 ? (1139 + i - 6) : 755 + i, -1);
				continue;
			}
			player.getPackets().sendGlobalConfig(
					i >= 6 ? (1139 + i - 6) : 755 + i, items[i]);
			String name = ItemDefinitions.getItemDefinitions(items[i])
					.getName();
			if (filter != null) {
				name = filter.rename(name);
			}
			player.getPackets().sendGlobalString(
					i >= 6 ? (280 + i - 6) : 132 + i, name);
		}
		setMaxQuantity(player, maxQuantity);
		setQuantity(player, maxQuantity);
        setItem(items);
    }

	public static void handleSetQuantityButtons(final Player player,
			final int componentId) {
		if (componentId == 5) {
			setQuantity(player, 1, false);
		} else if (componentId == 6) {
			setQuantity(player, 5, false);
		} else if (componentId == 7) {
			setQuantity(player, 10, false);
		} else if (componentId == 8) {
			setQuantity(player, getMaxQuantity(player), false);
		} else if (componentId == 19) {
			setQuantity(player, getQuantity(player) + 1, false);
		} else if (componentId == 20) {
			setQuantity(player, getQuantity(player) - 1, false);
		}
	}

	public static void setMaxQuantity(final Player player, final int maxQuantity) {
		player.getTemporaryAttributtes().put("SkillsDialogueMaxQuantity",
				maxQuantity);
		player.getPackets().sendConfigByFile(8094, maxQuantity);
	}

	public static void setQuantity(final Player player, final int quantity) {
		setQuantity(player, quantity, true);
	}

	public static void setQuantity(final Player player, int quantity,
			final boolean refresh) {
		final int maxQuantity = getMaxQuantity(player);
		if (quantity > maxQuantity) {
			quantity = maxQuantity;
		} else if (quantity < 0) {
			quantity = 0;
		}
		player.getTemporaryAttributtes()
		.put("SkillsDialogueQuantity", quantity);
		if (refresh) {
			player.getPackets().sendConfigByFile(8095, quantity);
		}
	}

	public static int getMaxQuantity(final Player player) {
		final Integer maxQuantity = (Integer) player.getTemporaryAttributtes()
				.get("SkillsDialogueMaxQuantity");
		if (maxQuantity == null)
			return 0;
		return maxQuantity;
	}

	public static int getQuantity(final Player player) {
		final Integer quantity = (Integer) player.getTemporaryAttributtes()
				.get("SkillsDialogueQuantity");
		if (quantity == null)
			return 0;
		return quantity;
	}

	public static int getItemSlot(final int componentId) {
		if (componentId < 14)
			return 0;
		return componentId - 14;
	}

    public interface ItemNameFilter {

        String rename(String name);
    }
}
