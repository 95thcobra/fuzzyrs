package com.rs.player;

import com.rs.content.actions.skills.Skills;
import com.rs.content.actions.skills.firemaking.Bonfire;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.utils.item.ItemExamines;
import com.rs.world.item.Item;
import com.rs.world.item.ItemsContainer;

import java.io.Serializable;

public final class Equipment implements Serializable {

	public static final byte SLOT_HAT = 0, SLOT_CAPE = 1, SLOT_AMULET = 2,
			SLOT_WEAPON = 3, SLOT_CHEST = 4, SLOT_SHIELD = 5, SLOT_LEGS = 7,
			SLOT_HANDS = 9, SLOT_FEET = 10, SLOT_RING = 12, SLOT_ARROWS = 13,
			SLOT_AURA = 14;
	static final int[] DISABLED_SLOTS = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 1, 1, 0};
	private static final long serialVersionUID = -4147163237095647617L;
	private final ItemsContainer<Item> items;
	private transient Player player;
	private transient int equipmentHpIncrease;

	public Equipment() {
		items = new ItemsContainer<Item>(15, false);
	}

	public static boolean hideArms(final Item item) {
		final String name = item.getName().toLowerCase();
		if
			// temp old graphics fix, but bugs alil new ones
				(name.contains("d'hide body")
				|| name.contains("dragonhide body")
				|| name.equals("stripy pirate shirt")
				|| (name.contains("chainbody") && (name.contains("iron")
				|| name.contains("bronze") || name.contains("steel")
				|| name.contains("black") || name.contains("mithril")
				|| name.contains("adamant") || name.contains("rune") || name
				.contains("white"))) || name.equals("leather body")
				|| name.equals("hardleather body")
				|| name.contains("studded body"))
			return false;
		return item.getDefinitions().getEquipType() == 6;
	}

	public static boolean hideHair(final Item item) {
		return item.getDefinitions().getEquipType() == 8;
	}

	public static boolean showBear(final Item item) {
		final String name = item.getName().toLowerCase();
		return !hideHair(item) || name.contains("horns")
				|| name.contains("hat") || name.contains("afro")
				|| name.contains("cowl") || name.contains("tattoo")
				|| name.contains("headdress") || name.contains("hood")
				|| (name.contains("mask") && !name.contains("h'ween"))
				|| (name.contains("helm") && !name.contains("full"));
	}

	public static int getItemSlot(final int itemId) {
		return ItemDefinitions.getItemDefinitions(itemId).getEquipSlot();
	}

	public static boolean isTwoHandedWeapon(final Item item) {
		return item.getDefinitions().getEquipType() == 5;
	}

	public void setPlayer(final Player player) {
		this.player = player;
	}

	public void init() {
		player.getPackets().sendItems(94, items);
		refresh(null);
	}

	public void refresh(final int... slots) {
		if (slots != null) {
			player.getPackets().sendUpdateItems(94, items, slots);
			player.getCombatDefinitions().checkAttackStyle();
		}
		player.getCombatDefinitions().refreshBonuses();
		refreshConfigs(slots == null);
	}

	public void refreshAll() {
		for (int i = 0; i <= 14; i++) {
			refresh(i);
		}
	}

	public void reset() {
		items.reset();
		init();
	}

	public Item getItem(final int slot) {
		return items.get(slot);
	}

	public void sendExamine(final int slotId) {
		final Item item = items.get(slotId);
		if (item == null)
			return;
		player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	public void refreshConfigs(final boolean init) {
		double hpIncrease = 0;
		for (int index = 0; index < items.getSize(); index++) {
			final Item item = items.get(index);
			if (item == null) {
				continue;
			}
			final int id = item.getId();
			if (index == Equipment.SLOT_HAT) {
				if (id == 20135 || id == 20137 // torva
						|| id == 20147 || id == 20149 // pernix
						|| id == 20159 || id == 20161 // virtus
				) {
					hpIncrease += 66;
				}

			} else if (index == Equipment.SLOT_CHEST) {
				if (id == 20139 || id == 20141 // torva
						|| id == 20151 || id == 20153 // pernix
						|| id == 20163 || id == 20165 // virtus
				) {
					hpIncrease += 200;
				}
			} else if (index == Equipment.SLOT_LEGS) {
				if (id == 20143 || id == 20145 // torva
						|| id == 20155 || id == 20157 // pernix
						|| id == 20167 || id == 20169 // virtus
				) {
					hpIncrease += 134;
				}
			}

		}
		if (player.getLastBonfire() > 0) {
			final int maxhp = player.getSkills().getLevel(Skills.HITPOINTS) * 10;
			hpIncrease += (maxhp * Bonfire.getBonfireBoostMultiplier(player))
					- maxhp;
		}
		if (player.getHpBoostMultiplier() != 0) {
			final int maxhp = player.getSkills().getLevel(Skills.HITPOINTS) * 10;
			hpIncrease += maxhp * player.getHpBoostMultiplier();
		}
		if (hpIncrease != equipmentHpIncrease) {
			equipmentHpIncrease = (int) hpIncrease;
			if (!init) {
				player.refreshHitPoints();
			}
		}
	}

	public int getWeaponRenderEmote() {
		final Item weapon = items.get(3);
		if (weapon == null)
			return 1426;
		return weapon.getDefinitions().getRenderAnimId();
	}

	public boolean hasShield() {
		return items.get(5) != null;
	}

	public int getWeaponId() {
		final Item item = items.get(SLOT_WEAPON);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getChestId() {
		final Item item = items.get(SLOT_CHEST);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getHatId() {
		final Item item = items.get(SLOT_HAT);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getShieldId() {
		final Item item = items.get(SLOT_SHIELD);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getLegsId() {
		final Item item = items.get(SLOT_LEGS);
		if (item == null)
			return -1;
		return item.getId();
	}

	public void removeAmmo(final int ammoId, final int ammount) {
		if (ammount == -1) {
			items.remove(SLOT_WEAPON, new Item(ammoId, 1));
			refresh(SLOT_WEAPON);
		} else {
			items.remove(SLOT_ARROWS, new Item(ammoId, ammount));
			refresh(SLOT_ARROWS);
		}
	}

	public int getAuraId() {
		final Item item = items.get(SLOT_AURA);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getCapeId() {
		final Item item = items.get(SLOT_CAPE);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getRingId() {
		final Item item = items.get(SLOT_RING);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getAmmoId() {
		final Item item = items.get(SLOT_ARROWS);
		if (item == null)
			return -1;
		return item.getId();
	}

	public void deleteItem(final int itemId, final int amount) {
		final Item[] itemsBefore = items.getItemsCopy();
		items.remove(new Item(itemId, amount));
		refreshItems(itemsBefore);
	}

	public void refreshItems(final Item[] itemsBefore) {
		final int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			if (itemsBefore[index] != items.getItems()[index]) {
				changedSlots[count++] = index;
			}
		}
		final int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
	}

	public int getBootsId() {
		final Item item = items.get(SLOT_FEET);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getGlovesId() {
		final Item item = items.get(SLOT_HANDS);
		if (item == null)
			return -1;
		return item.getId();
	}

	public ItemsContainer<Item> getItems() {
		return items;
	}

	public int getEquipmentHpIncrease() {
		return equipmentHpIncrease;
	}

	public void setEquipmentHpIncrease(final int hp) {
		this.equipmentHpIncrease = hp;
	}

	public boolean wearingArmour() {
		return getItem(SLOT_HAT) != null || getItem(SLOT_CAPE) != null
				|| getItem(SLOT_AMULET) != null || getItem(SLOT_WEAPON) != null
				|| getItem(SLOT_CHEST) != null || getItem(SLOT_SHIELD) != null
				|| getItem(SLOT_LEGS) != null || getItem(SLOT_HANDS) != null
				|| getItem(SLOT_FEET) != null;
	}

	public int getAmuletId() {
		final Item item = items.get(SLOT_AMULET);
		if (item == null)
			return -1;
		return item.getId();
	}

	public boolean hasTwoHandedWeapon() {
		final Item weapon = items.get(SLOT_WEAPON);
		return weapon != null && isTwoHandedWeapon(weapon);
	}
}
