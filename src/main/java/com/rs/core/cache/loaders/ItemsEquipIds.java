package com.rs.core.cache.loaders;

import com.rs.core.utils.Utils;

import java.util.HashMap;

public final class ItemsEquipIds {

	private static final HashMap<Integer, Integer> equipIds = new HashMap<Integer, Integer>();

	private ItemsEquipIds() {

	}

	public static void init() {
		int equipId = 0;
		for (int itemId = 0; itemId < Utils.getItemDefinitionsSize(); itemId++) {
			final ItemDefinitions def = ItemDefinitions
					.getItemDefinitions(itemId);
			if (def.getMaleWornModelId1() >= 0
					|| def.getFemaleWornModelId1() >= 0) {
				equipIds.put(itemId, equipId++);
			}
		}
	}

	public static int getEquipId(final int itemId) {
		final Integer equipId = equipIds.get(itemId);
		return equipId == null ? -1 : equipId;
	}
}
