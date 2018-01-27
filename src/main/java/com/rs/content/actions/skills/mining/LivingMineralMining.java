package com.rs.content.actions.skills.mining;

import com.rs.content.actions.skills.Skills;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.npc.others.LivingRock;

public class LivingMineralMining extends MiningBase {

	private final LivingRock rock;

	public LivingMineralMining(final LivingRock rock) {
		this.rock = rock;
	}

	@Override
	public boolean start(final Player player) {
		if (!checkAll(player))
			return false;
		setActionDelay(player, getMiningDelay(player));
		return true;
	}

	private int getMiningDelay(final Player player) {
		final int oreBaseTime = 50;
		final int oreRandomTime = 20;
		int mineTimer = oreBaseTime
				- player.getSkills().getLevel(Skills.MINING)
				- Utils.getRandom(pickaxeTime);
		if (mineTimer < 1 + oreRandomTime) {
			mineTimer = 1 + Utils.getRandom(oreRandomTime);
		}
		mineTimer /= player.getAuraManager().getMininingAccurayMultiplier();
		return mineTimer;
	}

	private boolean checkAll(final Player player) {
		if (!setPickaxe(player)) {
			player.getPackets().sendGameMessage(
					"You need a pickaxe to mine this rock.");
			return false;
		}
		if (!hasPickaxe(player)) {
			player.getPackets().sendGameMessage(
					"You dont have the required level to use this pickaxe.");
			return false;
		}
		if (!hasMiningLevel(player))
			return false;
		if (!player.getInventory().hasFreeSlots()) {
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return false;
		}
		if (!rock.canMine(player)) {
			player.getPackets()
					.sendGameMessage(
							"You must wait at least one minute before you can mine a living rock creature that someone else defeated.");
			return false;
		}
		return true;
	}

	private boolean hasMiningLevel(final Player player) {
		if (73 > player.getSkills().getLevel(Skills.MINING)) {
			player.getPackets().sendGameMessage(
					"You need a mining level of 73 to mine this rock.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(final Player player) {
		player.setNextAnimation(new Animation(emoteId));
		return checkRock(player);
	}

	@Override
	public int processWithDelay(final Player player) {
		addOre(player);
		rock.takeRemains();
		player.setNextAnimation(new Animation(-1));
		return -1;
	}

	private void addOre(final Player player) {
		player.getSkills().addXp(Skills.MINING, 25);
		player.getInventory().addItem(15263, Utils.random(5, 25));
		player.getPackets().sendGameMessage(
				"You manage to mine some living minerals.", true);
	}

	private boolean checkRock(final Player player) {
		return !rock.hasFinished();
	}
}
