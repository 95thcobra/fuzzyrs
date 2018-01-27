package com.rs.content.minigames.creations;

import com.rs.content.actions.Action;
import com.rs.content.actions.skills.Skills;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.WorldObject;
import com.rs.world.item.Item;

public class CreationSkillsAction extends Action {

	private final Animation animation;
	private final WorldObject object;
	private final int baseId;
	private final int objectIndex;
	private final Item itemUsed;
	private final int skillId;
	private CreationObjects definitions;
	public CreationSkillsAction(final WorldObject object,
			final Animation animation, final Item bestItem, final int baseId,
			final int objectIndex) {
		this.skillId = Skills.HUNTER;
		this.object = object;
		this.animation = animation;
		this.itemUsed = bestItem;
		this.baseId = baseId;
		this.objectIndex = objectIndex;
	}

	// 50 == class 4

	@Override
	public boolean start(final Player player) {
		definitions = CreationObjects.CLASS_5;
		if (player.getSkills().getLevel(
				StealingCreation.getRequestedObjectSkill()) < definitions
				.getLevel() || itemUsed == null)
			return false;
		setActionDelay(player, getSkillTimer(player, skillId));
		player.setNextFaceWorldTile(object);
		return true;
	}

	@Override
	public boolean process(final Player player) {
		if (itemUsed == null)
			return false;
		player.setNextAnimation(animation);
		return true;
	}

	@Override
	public int processWithDelay(final Player player) {
		if (!player.getInventory().hasFreeSlots()) {
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return -1;
		} else if (Utils.getRandom(definitions.getRandomLife()) == 0) {
			System.out.println("Empty");
		}
		player.getInventory().addItem(
				new Item(StealingCreation.SACRED_CLAY[objectIndex], 1));
		return getSkillTimer(player, skillId);
	}

	public int getSkillTimer(final Player player, final int skillId) {
		final int playerLevel = player.getSkills().getLevel(Skills.FISHING);
		final int fishLevel = definitions.level;
		final int modifier = itemUsed.getDefinitions()
				.getWearingSkillRequiriments().get(skillId);
		final int randomAmt = Utils.random(4);
		double cycleCount = 1;
		final double otherBonus = 0;
		cycleCount = Math
				.ceil(((fishLevel + otherBonus) * 50 - playerLevel * 10)
						/ modifier * 0.25 - randomAmt * 4);
		if (cycleCount < 1) {
			cycleCount = 1;
		}
		int delay = (int) cycleCount + 1;
		delay /= player.getAuraManager().getFishingAccurayMultiplier();
		return delay;
	}

	@Override
	public void stop(final Player player) {
		this.setActionDelay(player, 3);
	}

	public enum CreationObjects {

		CLASS_1(10, -1, 1, 1), // doesnt run out copper

		CLASS_2(20, 200, 6, 20), // silver

		CLASS_3(25, 300, 12, 40), // mithril

		CLASS_4(30, 400, 16, 60), // adamant

		CLASS_5(35, 500, 20, 80); // rune

		private int baseTime, randomTime, randomLife, level;

		CreationObjects(final int baseTime, final int randomLife,
						final int randomTime, final int level) {
			this.baseTime = baseTime;
			this.randomTime = randomTime;
			this.randomLife = randomLife;
			this.level = level;
		}

		public int getBaseTime() {
			return baseTime;
		}

		public int getRandomTime() {
			return randomTime;
		}

		public int getRandomLife() {
			return randomLife;
		}

		public int getLevel() {
			return level;
		}
	}
}
