package com.rs.content.actions.skills.prayer;

import com.rs.content.actions.skills.Skills;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.core.utils.Logger;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Graphics;
import com.rs.world.item.Item;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.HashMap;
import java.util.Map;

public class GildedAltar {

	public enum bonestoOffer {

		NORMAL(526, 200),

		BIG(532, 300),

		OURG(4834, 500),

		DRAGON(536, 520),

		DAG(6729, 900),

		FROST_DRAGON(18830, 750);

		public static boolean stopOfferGod = false;
		private static Map<Integer, bonestoOffer> bones = new HashMap<Integer, bonestoOffer>();
		private static boolean imfuckingprayingfortheGods = false;

		static {
			for (final bonestoOffer bone : bonestoOffer.values()) {
				bones.put(bone.getId(), bone);
			}
		}

		private final int id;
		private final double experience;

		bonestoOffer(final int id, final double experience) {
			this.id = id;
			this.experience = experience;
		}

		public static bonestoOffer forId(final int id) {
			return bones.get(id);
		}

		public static void offerprayerGod(final Player player, final Item item) {
			final int itemId = item.getId();
			final ItemDefinitions itemDef = new ItemDefinitions(item.getId());
			final bonestoOffer bone = bonestoOffer.forId(item.getId());
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					try {
						if (!player.getInventory().containsItem(itemId, 1)) {
							stop();
							return;
						}
						player.getPackets()
								.sendGameMessage(
										"The gods are very pleased with your offering.");
						player.setNextAnimation(new Animation(896));
						// player.getPackets().sendGraphics(new Graphics(624),
						// object);
						player.setNextGraphics(new Graphics(624));
						player.getInventory().deleteItem(new Item(itemId, 1));
						final double xp = bone.getExperience()
								* player.getAuraManager().getPrayerMultiplier();
						player.getSkills().addXp(Skills.PRAYER, xp);
						player.getPackets().sendSound(2738, 0, 1);
						player.getInventory().refresh();
					} catch (final Throwable e) {
						Logger.handle(e);
					}
				}
			}, 0, 3);
		}

		public int getId() {
			return id;
		}

		public double getExperience() {
			return experience;
		}
	}
}
