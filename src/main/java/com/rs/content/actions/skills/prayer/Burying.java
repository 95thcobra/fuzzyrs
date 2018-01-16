package com.rs.content.actions.skills.prayer;

import com.rs.content.actions.skills.Skills;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.item.Item;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.HashMap;
import java.util.Map;

public class Burying {

	public enum Bone {
		NORMAL(526, 150),

		BIG(532, 300),

		DRAGON(536, 430),

		OURG_BONE(4834, 420),

		OURG_BONES(14793, 500),
		
		DAG(6729, 650),

		FROST_DRAGON(18830, 500);

		public static final Animation BURY_ANIMATION = new Animation(827);
		private static Map<Integer, Bone> bones = new HashMap<Integer, Bone>();

		static {
			for (final Bone bone : Bone.values()) {
				bones.put(bone.getId(), bone);
			}
		}

		private final int id;
		private final double experience;

		Bone(final int id, final double experience) {
			this.id = id;
			this.experience = experience;
		}

		public static Bone forId(final int id) {
			return bones.get(id);
		}

		public static void bury(final Player player, final int inventorySlot) {
			final Item item = player.getInventory().getItem(inventorySlot);
			if (item == null || Bone.forId(item.getId()) == null)
				return;
			if (player.getBoneDelay() > Utils.currentTimeMillis())
				return;
			final Bone bone = Bone.forId(item.getId());
			final ItemDefinitions itemDef = new ItemDefinitions(item.getId());
			player.addBoneDelay(3000);
			player.getPackets().sendSound(2738, 0, 1);
			player.setNextAnimation(new Animation(827));
			player.isBurying = true;
			player.getPackets().sendGameMessage(
					"You dig a hole in the ground...");
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.getPackets().sendGameMessage(
							"You bury the " + itemDef.getName().toLowerCase());
					player.getInventory().deleteItem(item.getId(), 1);
					player.getSkills().addXp(Skills.PRAYER,
							bone.getExperience());
					stop();
					player.isBurying = false;
				}

			}, 2);
		}

		public int getId() {
			return id;
		}

		public double getExperience() {
			return experience;
		}
	}
}
