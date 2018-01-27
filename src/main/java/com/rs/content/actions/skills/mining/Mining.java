package com.rs.content.actions.skills.mining;

import com.rs.content.actions.skills.Skills;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.WorldObject;

public final class Mining extends MiningBase {

	private final WorldObject rock;
	private final RockDefinitions definitions;
	private boolean usedDeplateAurora;

	public Mining(final WorldObject rock, final RockDefinitions definitions) {
		this.rock = rock;
		this.definitions = definitions;
	}

	@Override
	public boolean start(final Player player) {
		if (!checkAll(player))
			return false;
		player.getPackets().sendGameMessage(
				"You swing your pickaxe at the rock.", true);
		setActionDelay(player, getMiningDelay(player));
		return true;
	}

	private int getMiningDelay(final Player player) {
		int summoningBonus = 0;
		if (player.getFamiliar() != null) {
			if (player.getFamiliar().getId() == 7342
					|| player.getFamiliar().getId() == 7342) {
				summoningBonus += 10;
			} else if (player.getFamiliar().getId() == 6832
					|| player.getFamiliar().getId() == 6831) {
				summoningBonus += 1;
			}
		}
		int mineTimer = definitions.getOreBaseTime()
				- (player.getSkills().getLevel(Skills.MINING) + summoningBonus)
				- Utils.getRandom(pickaxeTime);
		if (mineTimer < 1 + definitions.getOreRandomTime()) {
			mineTimer = 1 + Utils.getRandom(definitions.getOreRandomTime());
		}
		mineTimer /= player.getAuraManager().getMininingAccurayMultiplier();
		return mineTimer;
	}

	private boolean checkAll(final Player player) {
		if (!hasPickaxe(player)) {
			player.getPackets().sendGameMessage(
					"You need a pickaxe to mine this rock.");
			return false;
		}
		if (!setPickaxe(player)) {
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
		return true;
	}

	private boolean hasMiningLevel(final Player player) {
		if (definitions.getLevel() > player.getSkills().getLevel(Skills.MINING)) {
			player.getPackets().sendGameMessage(
					"You need a mining level of " + definitions.getLevel()
							+ " to mine this rock.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(final Player player) {
		if (player.isChillBlastMining()) {
			player.setNextAnimation(new Animation(17310));
			player.setNextGraphics(new Graphics(3304));
			return checkRock(player);
		} else {
			player.setNextAnimation(new Animation(emoteId));
		}
		return checkRock(player);
	}

	@Override
	public int processWithDelay(final Player player) {
		addOre(player);
		if (definitions.getEmptyId() != -1) {
			if (!usedDeplateAurora
					&& (1 + Math.random()) < player.getAuraManager()
							.getChanceNotDepleteMN_WC()) {
				usedDeplateAurora = true;
			} else if (Utils.getRandom(definitions.getRandomLifeProbability()) == 0) {
				if (rock.spawnedByEd) {
					World.spawnTemporaryObject(
							new WorldObject(definitions.getEmptyId(), rock
									.getType(), rock.getRotation(),
									rock.getX(), rock.getY(), rock.getPlane()),
							definitions.respawnDelay * 600, false, rock);
				} else {
					World.spawnTemporaryObject(
							new WorldObject(definitions.getEmptyId(), rock
									.getType(), rock.getRotation(),
									rock.getX(), rock.getY(), rock.getPlane()),
							definitions.respawnDelay * 600, false);
				}
				player.setNextAnimation(new Animation(-1));
				return -1;
			}
		}
		if (!player.getInventory().hasFreeSlots()
				&& definitions.getOreId() != -1) {
			player.setNextAnimation(new Animation(-1));
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return -1;
		}
		return getMiningDelay(player);
	}

	private void addOre(final Player player) {
		double xpBoost = 0;
		int idSome = 0;
		if (definitions == RockDefinitions.Granite_Ore) {
			idSome = Utils.getRandom(2) * 2;
			if (idSome == 2) {
				xpBoost += 10;
			} else if (idSome == 4) {
				xpBoost += 25;
			}
		} else if (definitions == RockDefinitions.Sandstone_Ore) {
			idSome = Utils.getRandom(3) * 2;
			xpBoost += idSome / 2 * 10;
		}
		double totalXp = definitions.getXp() + xpBoost;
		if (hasMiningSuit(player)) {
			totalXp *= 1.025;
		}
		player.getSkills().addXp(Skills.MINING, totalXp);
		if (definitions.getOreId() != -1) {
			player.getInventory().addItem(definitions.getOreId() + idSome, 1);
			final String oreName = ItemDefinitions
					.getItemDefinitions(definitions.getOreId() + idSome)
					.getName().toLowerCase();
			player.getPackets().sendGameMessage(
					"You mine some " + oreName + ".", true);
		}
	}

	private boolean hasMiningSuit(final Player player) {
		return player.getEquipment().getHatId() == 20789
				&& player.getEquipment().getChestId() == 20791
				&& player.getEquipment().getLegsId() == 20790
				&& player.getEquipment().getBootsId() == 20788;
	}

	private boolean checkRock(final Player player) {
		return World.getRegion(rock.getRegionId()).containsObject(rock.getId(),
				rock);
	}

	public enum RockDefinitions {

		Clay_Ore(1, 5, 434, 10, 1, 11552, 5, 0), Copper_Ore(1, 17.5, 436, 10,
				1, 11552, 5, 0), Tin_Ore(1, 17.5, 438, 15, 1, 11552, 5, 0), Iron_Ore(
				15, 35, 440, 15, 1, 11552, 10, 0), Sandstone_Ore(35, 30, 6971,
				30, 1, 11552, 10, 0), Silver_Ore(20, 40, 442, 25, 1, 11552, 20,
				0), Coal_Ore(30, 50, 453, 50, 10, 11552, 30, 0), Granite_Ore(
				45, 50, 6979, 50, 10, 11552, 20, 0), Gold_Ore(40, 60, 444, 80,
				20, 11554, 40, 0), Mithril_Ore(55, 80, 447, 100, 20, 11552, 60,
				0), Adamant_Ore(70, 95, 449, 130, 25, 11552, 180, 0), Runite_Ore(
				85, 125, 451, 150, 30, 11552, 360, 0), LRC_Coal_Ore(77, 50,
				453, 50, 10, -1, -1, -1), CRASHED_STAR(70, 65, 13727, 2, 30,
				-1, -1, -1), LRC_Gold_Ore(80, 60, 444, 40, 10, -1, -1, -1);

		private int level;
		private double xp;
		private int oreId;
		private int oreBaseTime;
		private int oreRandomTime;
		private int emptySpot;
		private int respawnDelay;
		private int randomLifeProbability;

		RockDefinitions(final int level, final double xp,
						final int oreId, final int oreBaseTime,
						final int oreRandomTime, final int emptySpot,
						final int respawnDelay, final int randomLifeProbability) {
			this.level = level;
			this.xp = xp;
			this.oreId = oreId;
			this.oreBaseTime = oreBaseTime;
			this.oreRandomTime = oreRandomTime;
			this.emptySpot = emptySpot;
			this.respawnDelay = respawnDelay;
			this.randomLifeProbability = randomLifeProbability;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public int getOreId() {
			return oreId;
		}

		public int getOreBaseTime() {
			return oreBaseTime;
		}

		public int getOreRandomTime() {
			return oreRandomTime;
		}

		public int getEmptyId() {
			return emptySpot;
		}

		public int getRespawnDelay() {
			return respawnDelay;
		}

		public int getRandomLifeProbability() {
			return randomLifeProbability;
		}
	}

}
