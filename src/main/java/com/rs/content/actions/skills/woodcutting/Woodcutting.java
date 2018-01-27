package com.rs.content.actions.skills.woodcutting;

import com.rs.content.actions.Action;
import com.rs.content.actions.skills.Skills;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.*;

import java.util.Random;

public final class Woodcutting extends Action {

	private static final int[] NESTS = {5070, 5071, 5072, 5073, 5074};
	private final WorldObject tree;
	private final TreeDefinitions definitions;
	private final boolean usingBeaver = false;
	private int emoteId;
	private int axeTime;
	private boolean usedDeplateAurora;

	public Woodcutting(final WorldObject tree, final TreeDefinitions definitions) {
		this.tree = tree;
		this.definitions = definitions;
	}

	@Override
	public boolean start(final Player player) {
		if (!checkAll(player))
			return false;
		player.getPackets()
		.sendGameMessage(
				usingBeaver ? "Your beaver uses its strong teeth to chop down the tree..."
						: "You swing your hatchet at the "
						+ (TreeDefinitions.IVY == definitions ? "ivy"
								: "tree") + "...", true);
		setActionDelay(player, getWoodcuttingDelay(player));
		return true;
	}

	private int getWoodcuttingDelay(final Player player) {
		final int summoningBonus = player.getFamiliar() != null ? (player
				.getFamiliar().getId() == 6808 || player.getFamiliar().getId() == 6807) ? 10
						: 0
						: 0;
		int wcTimer = definitions.getLogBaseTime()
				- (player.getSkills().getLevel(8) + summoningBonus)
				- Utils.getRandom(axeTime);
		if (wcTimer < 1 + definitions.getLogRandomTime()) {
			wcTimer = 1 + Utils.getRandom(definitions.getLogRandomTime());
		}
		wcTimer /= player.getAuraManager().getWoodcuttingAccurayMultiplier();
		return wcTimer;
	}

	private boolean checkAll(final Player player) {
		if (!hasAxe(player)) {
			player.getPackets().sendGameMessage(
					"You need a hatchet to chop down this tree.");
			return false;
		}
		if (!setAxe(player)) {
			player.getPackets().sendGameMessage(
					"You dont have the required level to use that axe.");
			return false;
		}
		if (!hasWoodcuttingLevel(player))
			return false;
		if (!player.getInventory().hasFreeSlots()) {
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return false;
		}
		return true;
	}

	private boolean hasWoodcuttingLevel(final Player player) {
		if (definitions.getLevel() > player.getSkills().getLevel(8)) {
			player.getPackets().sendGameMessage(
					"You need a woodcutting level of " + definitions.getLevel()
					+ " to chop down this tree.");
			return false;
		}
		return true;
	}

	private boolean setAxe(final Player player) {
		final int level = player.getSkills().getLevel(8);
		final int weaponId = player.getEquipment().getWeaponId();
		if (weaponId != -1) {
			switch (weaponId) {
			case 6739: // dragon axe
				if (level >= 61) {
					emoteId = 2846;
					axeTime = 13;
					return true;
				}
				break;
			case 1359: // rune axe
				if (level >= 41) {
					emoteId = 867;
					axeTime = 10;
					return true;
				}
				break;
			case 1357: // adam axe
				if (level >= 31) {
					emoteId = 869;
					axeTime = 7;
					return true;
				}
				break;
			case 1355: // mit axe
				if (level >= 21) {
					emoteId = 871;
					axeTime = 5;
					return true;
				}
				break;
			case 1361: // black axe
				if (level >= 11) {
					emoteId = 873;
					axeTime = 4;
					return true;
				}
				break;
			case 1353: // steel axe
				if (level >= 6) {
					emoteId = 875;
					axeTime = 3;
					return true;
				}
				break;
			case 1349: // iron axe
				emoteId = 877;
				axeTime = 2;
				return true;
			case 1351: // bronze axe
				emoteId = 879;
				axeTime = 1;
				return true;
			case 13661: // Inferno adze
				if (level >= 61) {
					emoteId = 10251;
					axeTime = 13;
					return true;
				}
				break;
			}
		}
		if (player.getInventory().containsOneItem(6739)) {
			if (level >= 61) {
				emoteId = 2846;
				axeTime = 13;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1359)) {
			if (level >= 41) {
				emoteId = 867;
				axeTime = 10;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1357)) {
			if (level >= 31) {
				emoteId = 869;
				axeTime = 7;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1355)) {
			if (level >= 21) {
				emoteId = 871;
				axeTime = 5;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1361)) {
			if (level >= 11) {
				emoteId = 873;
				axeTime = 4;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1353)) {
			if (level >= 6) {
				emoteId = 875;
				axeTime = 3;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1349)) {
			emoteId = 877;
			axeTime = 2;
			return true;
		}
		if (player.getInventory().containsOneItem(1351)) {
			emoteId = 879;
			axeTime = 1;
			return true;
		}
		if (player.getInventory().containsOneItem(13661)) {
			if (level >= 61) {
				emoteId = 10251;
				axeTime = 13;
				return true;
			}
		}
		return false;

	}

	private boolean hasAxe(final Player player) {
		if (player.getInventory().containsOneItem(1351, 1349, 1353, 1355, 1357,
				1361, 1359, 6739, 13661))
			return true;
		final int weaponId = player.getEquipment().getWeaponId();
		if (weaponId == -1)
			return false;
		switch (weaponId) {
		case 1351:// Bronze Axe
		case 1349:// Iron Axe
		case 1353:// Steel Axe
		case 1361:// Black Axe
		case 1355:// Mithril Axe
		case 1357:// Adamant Axe
		case 1359:// Rune Axe
		case 6739:// Dragon Axe
		case 13661: // Inferno adze
			return true;
		default:
			return false;
		}

	}

	@Override
	public boolean process(final Player player) {
		player.setNextAnimation(new Animation(usingBeaver ? 1 : emoteId));
		return checkTree(player);
	}

	@Override
	public int processWithDelay(final Player player) {
		addLog(player);
		if (!usedDeplateAurora
				&& (1 + Math.random()) < player.getAuraManager()
				.getChanceNotDepleteMN_WC()) {
			usedDeplateAurora = true;
		} else if (Utils.getRandom(definitions.getRandomLifeProbability()) == 0) {
			final long time = definitions.respawnDelay * 600;
			if (tree.spawnedByEd) {
				World.spawnTemporaryObject(
						new WorldObject(definitions.getStumpId(), tree
								.getType(), tree.getRotation(), tree.getX(),
								tree.getY(), tree.getPlane()), time, false,
						tree);
			} else {
				World.spawnTemporaryObject(
						new WorldObject(definitions.getStumpId(), tree
								.getType(), tree.getRotation(), tree.getX(),
								tree.getY(), tree.getPlane()), time);
			}
			if (tree.getPlane() < 3 && definitions != TreeDefinitions.IVY) {
				WorldObject object = World.getObject(new WorldTile(
						tree.getX() - 1, tree.getY() - 1, tree.getPlane() + 1));

				if (object == null) {
					object = World.getObject(new WorldTile(tree.getX(), tree
							.getY() - 1, tree.getPlane() + 1));
					if (object == null) {
						object = World.getObject(new WorldTile(tree.getX() - 1,
								tree.getY(), tree.getPlane() + 1));
						if (object == null) {
							object = World.getObject(new WorldTile(tree.getX(),
									tree.getY(), tree.getPlane() + 1));
						}
					}
				}

				if (object != null) {
					World.removeTemporaryObject(object, time, false);
				}
			}
			player.setNextAnimation(new Animation(-1));
			return -1;
		}
		if (!player.getInventory().hasFreeSlots()) {
			player.setNextAnimation(new Animation(-1));
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return -1;
		}
		return getWoodcuttingDelay(player);
	}

	private void addLog(final Player player) {
		double xpBoost = 1.00;
		if (player.getEquipment().getChestId() == 10939) {
			xpBoost += 0.008;
		}
		if (player.getEquipment().getLegsId() == 10940) {
			xpBoost += 0.006;
		}
		if (player.getEquipment().getHatId() == 10941) {
			xpBoost += 0.004;
		}
		if (player.getEquipment().getBootsId() == 10933) {
			xpBoost += 0.002;
		}
		if (player.getEquipment().getChestId() == 10939
				&& player.getEquipment().getLegsId() == 10940
				&& player.getEquipment().getHatId() == 10941
				&& player.getEquipment().getBootsId() == 10933) {
			xpBoost += 0.005;
		}
		player.getSkills().addXp(8, definitions.getXp() * xpBoost);
		player.getInventory().addItem(definitions.getLogsId(), 1);
		final int random = NESTS[Utils.random(NESTS.length - 1)];
		final Random Chance = new Random();
		final int roll = Chance.nextInt(100);
		if (roll < 5) {
			player.getInventory().addItem(random, 1);
			player.getPackets().sendGameMessage(
					"A bird nest fell out of the tree.");
		}
		if (player.getEquipment().getWeaponId() == 13661
				&& !(definitions == TreeDefinitions.IVY)) {
			if (Utils.getRandom(3) == 0) {
				final String logName = ItemDefinitions
						.forId(definitions.getLogsId()).getName().toLowerCase();
				player.getSkills().addXp(Skills.FIREMAKING,
						definitions.getXp() * 1);
				player.getInventory().deleteItem(definitions.logsId, 1);
				player.getPackets().sendGameMessage(
						"The adze's heat instantly incinerates the " + logName
								+ ".");
				player.setNextGraphics(new Graphics(1776));
			}
		}
		if (definitions == TreeDefinitions.IVY) {
			player.getPackets().sendGameMessage(
					"You succesfully cut an ivy vine.", true);
			// todo gfx
		} else {
			final String logName = ItemDefinitions
					.forId(definitions.getLogsId()).getName().toLowerCase();
			player.getPackets().sendGameMessage(
					"You get some " + logName + ".", true);
			// todo infernal adze
		}
	}

	private boolean checkTree(final Player player) {
		return World.getRegion(tree.getRegionId()).containsObject(tree.getId(),
				tree);
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}

	public enum TreeDefinitions {

		NORMAL(1, 25, 1511, 20, 4, 1341, 8, 0), // TODO

		EVERGREEN(1, 25, 1511, 20, 4, 57931, 8, 0),

		DEAD(1, 25, 1511, 20, 4, 12733, 8, 0),

		OAK(15, 37.5, 1521, 30, 4, 1341, 15, 15), // TODO

		WILLOW(30, 67.5, 1519, 60, 4, 1341, 51, 15), // TODO

		MAPLE(45, 100, 1517, 83, 16, 31057, 72, 10),

		YEW(60, 45, 1515, 120, 17, 1341, 94, 10), // TODO

		IVY(68, 332.5, -1, 120, 17, 46319, 58, 10),

		MAGIC(75, 78, 1513, 150, 21, 37824, 121, 10),

		CURSED_MAGIC(82, 250, 1513, 150, 21, 37822, 121, 10);

		private int level;
		private double xp;
		private int logsId;
		private int logBaseTime;
		private int logRandomTime;
		private int stumpId;
		private int respawnDelay;
		private int randomLifeProbability;

		TreeDefinitions(final int level, final double xp,
						final int logsId, final int logBaseTime,
						final int logRandomTime, final int stumpId,
						final int respawnDelay, final int randomLifeProbability) {
			this.level = level;
			this.xp = xp;
			this.logsId = logsId;
			this.logBaseTime = logBaseTime;
			this.logRandomTime = logRandomTime;
			this.stumpId = stumpId;
			this.respawnDelay = respawnDelay;
			this.randomLifeProbability = randomLifeProbability;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public int getLogsId() {
			return logsId;
		}

		public int getLogBaseTime() {
			return logBaseTime;
		}

		public int getLogRandomTime() {
			return logRandomTime;
		}

		public int getStumpId() {
			return stumpId;
		}

		public int getRespawnDelay() {
			return respawnDelay;
		}

		public int getRandomLifeProbability() {
			return randomLifeProbability;
		}
	}

}