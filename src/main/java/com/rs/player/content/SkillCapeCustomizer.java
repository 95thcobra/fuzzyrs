package com.rs.player.content;

import java.util.Arrays;

import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.player.Player;

public final class SkillCapeCustomizer {

	private SkillCapeCustomizer() {

	}

	public static void resetSkillCapes(final Player player) {
		player.setMaxedCapeCustomized(Arrays.copyOf(
				ItemDefinitions.getItemDefinitions(20767).originalModelColors,
				4));
		player.setCompletionistCapeCustomized(Arrays.copyOf(
				ItemDefinitions.getItemDefinitions(20769).originalModelColors,
				4));
	}

	public static void startCustomizing(final Player player, final int itemId) {
		player.getTemporaryAttributtes().put("SkillcapeCustomizeId", itemId);
		final int[] skillCape = itemId == 20767 ? player
				.getMaxedCapeCustomized() : player
				.getCompletionistCapeCustomized();
		player.getInterfaceManager().sendInterface(20);
		for (int i = 0; i < 4; i++) {
			player.getPackets().sendConfigByFile(9254 + i, skillCape[i]);
		}
		player.getPackets().sendIComponentModel(
				20,
				55,
				player.getAppearance().isMale() ? ItemDefinitions
						.getItemDefinitions(itemId).getMaleWornModelId1()
						: ItemDefinitions.getItemDefinitions(itemId)
						.getFemaleWornModelId1());
	}

	public static int getCapeId(final Player player) {
		final Integer id = (Integer) player.getTemporaryAttributtes().get(
				"SkillcapeCustomizeId");
		if (id == null)
			return -1;
		return id;
	}

	public static void handleSkillCapeCustomizerColor(final Player player,
			final int colorId) {
		final int capeId = getCapeId(player);
		if (capeId == -1)
			return;
		final Integer part = (Integer) player.getTemporaryAttributtes().get(
				"SkillcapeCustomize");
		if (part == null)
			return;
		final int[] skillCape = capeId == 20767 ? player
				.getMaxedCapeCustomized() : player
				.getCompletionistCapeCustomized();
		skillCape[part] = colorId;
		player.getPackets().sendConfigByFile(9254 + part, colorId);
		player.getInterfaceManager().sendInterface(20);
	}

	public static void handleSkillCapeCustomizer(final Player player,
			final int buttonId) {
		final int capeId = getCapeId(player);
		if (capeId == -1)
			return;
		final int[] skillCape = capeId == 20767 ? player
				.getMaxedCapeCustomized() : player
				.getCompletionistCapeCustomized();
		if (buttonId == 58) { // reset
			if (capeId == 20767) {
				player.setMaxedCapeCustomized(Arrays.copyOf(
						ItemDefinitions.getItemDefinitions(capeId).originalModelColors,
						4));
			} else {
				player.setCompletionistCapeCustomized(Arrays.copyOf(
						ItemDefinitions.getItemDefinitions(capeId).originalModelColors,
						4));
			}
			for (int i = 0; i < 4; i++) {
				player.getPackets().sendConfigByFile(9254 + i, skillCape[i]);
			}
		} else if (buttonId == 34) { // detail top
			player.getTemporaryAttributtes().put("SkillcapeCustomize", 0);
			player.getInterfaceManager().sendInterface(19);
			player.getPackets().sendConfig(2174, skillCape[0]);
		} else if (buttonId == 71) { // background top
			player.getTemporaryAttributtes().put("SkillcapeCustomize", 1);
			player.getInterfaceManager().sendInterface(19);
			player.getPackets().sendConfig(2174, skillCape[1]);
		} else if (buttonId == 83) { // detail button
			player.getTemporaryAttributtes().put("SkillcapeCustomize", 2);
			player.getInterfaceManager().sendInterface(19);
			player.getPackets().sendConfig(2174, skillCape[2]);
		} else if (buttonId == 95) { // background button
			player.getTemporaryAttributtes().put("SkillcapeCustomize", 3);
			player.getInterfaceManager().sendInterface(19);
			player.getPackets().sendConfig(2174, skillCape[3]);
		} else if (buttonId == 114 || buttonId == 142) { // done / close
			player.getAppearance().generateAppearenceData();
			player.closeInterfaces();
		}
	}
}
