package com.rs.world;

import com.rs.server.Server;
import com.rs.content.actions.skills.hunter.BoxAction;
import com.rs.content.actions.skills.mining.LivingRockCavern;
import com.rs.content.economy.exchange.GrandExchange;
import com.rs.content.minigames.GodWarsBosses;
import com.rs.content.minigames.ZarosGodwars;
import com.rs.content.minigames.clanwars.FfaZone;
import com.rs.content.minigames.clanwars.RequestController;
import com.rs.content.minigames.duel.DuelController;
import com.rs.content.minigames.soulwars.SoulWarsManager;
import com.rs.content.player.PlayerRank;
import com.rs.core.cores.CoresManager;
import com.rs.core.file.managers.IPBanFileManager;
import com.rs.core.file.managers.PkRankFileManager;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.player.content.DwarfCannon;
import com.rs.player.content.ShootingStar;
import com.rs.player.controlers.Wilderness;
import com.rs.world.item.FloorItem;
import com.rs.world.item.Item;
import com.rs.world.item.ItemConstants;
import com.rs.world.npc.NPC;
import com.rs.world.npc.corp.CorporealBeast;
import com.rs.world.npc.dragons.KingBlackDragon;
import com.rs.world.npc.godwars.GodWarMinion;
import com.rs.world.npc.godwars.armadyl.GodwarsArmadylFaction;
import com.rs.world.npc.godwars.armadyl.KreeArra;
import com.rs.world.npc.godwars.bandos.GeneralGraardor;
import com.rs.world.npc.godwars.bandos.GodwarsBandosFaction;
import com.rs.world.npc.godwars.saradomin.CommanderZilyana;
import com.rs.world.npc.godwars.saradomin.GodwarsSaradominFaction;
import com.rs.world.npc.godwars.zammorak.GodwarsZammorakFaction;
import com.rs.world.npc.godwars.zammorak.KrilTstsaroth;
import com.rs.world.npc.godwars.zaros.Nex;
import com.rs.world.npc.godwars.zaros.NexMinion;
import com.rs.world.npc.kalph.*;
import com.rs.world.npc.nomad.FlameVortex;
import com.rs.world.npc.nomad.Nomad;
import com.rs.world.npc.others.*;
import com.rs.world.npc.slayer.GanodermicBeast;
import com.rs.world.npc.slayer.Strykewyrm;
import com.rs.world.npc.sorgar.Elemental;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.*;
import java.util.concurrent.TimeUnit;

public final class World {

	private static final EntityList<Player> players = new EntityList<>(
			GameConstants.PLAYERS_LIMIT);
	private static final EntityList<NPC> npcs = new EntityList<>(
			GameConstants.NPCS_LIMIT);
	private static final Map<Integer, Region> regions = Collections
			.synchronizedMap(new HashMap<>());
	public static int exiting_delay;
	public static long exiting_start;
	public static List<WorldTile> restrictedTiles = new ArrayList<>();
	public static int star = 0;

	private static SoulWarsManager soulWarsManager;

	// private static final Object lock = new Object();
	private static boolean checkAgility;

	private World() {

	}

	/*
	 * private static void addLogicPacketsTask() {
	 * CoresManager.FAST_EXECUTOR.scheduleAtFixedRate(new TimerTask() {
	 *
	 * @Override public void run() { for(Player player : World.getPlayers()) {
	 * if(!player.hasStarted() || player.hasFinished()) continue;
	 * player.processLogicPackets(); } }
	 *
	 * }, 300, 300); }
	 */

	public static void deleteObject(final WorldTile tile) {
		restrictedTiles.add(tile);
	}

	public static void init() {
		soulWarsManager = new SoulWarsManager();
		soulWarsManager.start();
		GameTaskManager.init();
		LivingRockCavern.init();
	}

	public static SoulWarsManager getSoulWars() {
		return soulWarsManager;
	}

	public static void sendGlobalMessage(final String msg) {
		for (final Player player : getPlayers()) {
			player.getPackets().sendGameMessage(msg);
		}
	}

	public static boolean getCheckAgility() {
		return checkAgility;
	}

	public static void setCheckAgility(final boolean b) {
		checkAgility = b;
	}

	public static Map<Integer, Region> getRegions() {
		// synchronized (lock) {
		return regions;
		// }
	}

	public static Region getRegion(final int id) {
		return getRegion(id, false);
	}

	public static Region getRegion(final int id, final boolean load) {
		// synchronized (lock) {
		Region region = regions.get(id);
		if (region == null) {
			region = new Region(id);
			regions.put(id, region);
		}
		if (load) {
			region.checkLoadMap();
		}
		return region;
		// }
	}

	public static void addPlayer(final Player player) {
		players.add(player);
	}

	public static void removePlayer(final Player player) {
		players.remove(player);
	}

	public static void addNPC(final NPC npc) {
		npcs.add(npc);
	}

	public static void removeNPC(final NPC npc) {
		npcs.remove(npc);
	}

	public static NPC spawnNPC(final int id, final WorldTile tile,
			final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea,
			final boolean spawned) {
		NPC n = null;
		final BoxAction.HunterNPC hunterNPCs = BoxAction.HunterNPC.forId(id);
		if (hunterNPCs != null) {
			if (id == hunterNPCs.getNpcId()) {
				n = new ItemHunterNPC(id, tile, mapAreaNameHash,
						canBeAttackFromOutOfArea, spawned);
			}
		} else if (id == 6142 || id == 6144 || id == 6145 || id == 6143) {
			n = new PestMonsters(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id >= 5533 && id <= 5558) {
			n = new Elemental(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 7134) {
			n = new Bork(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		} else if (id == 9441) {
			n = new FlameVortex(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id >= 8832 && id <= 8834) {
			n = new LivingRock(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id >= 13465 && id <= 13481) {
			n = new Revenant(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 1158 || id == 1160) {
			n = new KalphiteQueen(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 907) {
			n = new Kolo1(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		} else if (id == 11301) {
			n = new Kolo3(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		} else if (id == 11302) {
			n = new Kolo4(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		} else if (id == 911) {
			n = new Kolo5(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		} else if (id >= 8528 && id <= 8532) {
			n = new Nomad(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		} else if (id == 6215 || id == 6211 || id == 3406 || id == 6216
				|| id == 6214 || id == 6212 || id == 6219
				|| id == 6221 || id == 6218) {
			n = new GodwarsZammorakFaction(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 6254 && id == 6259) {
			n = new GodwarsSaradominFaction(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 6246 || id == 6236 || id == 6232 || id == 6240
				|| id == 6241 || id == 6242 || id == 6235 || id == 6234
				|| id == 6243 || id == 6236 || id == 6244 || id == 6237
				|| id == 6246 || id == 6238 || id == 6239 || id == 6230) {
			n = new GodwarsArmadylFaction(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 6281 || id == 6282 || id == 6275 || id == 6279
				|| id == 9184 || id == 6268 || id == 6270 || id == 6274
				|| id == 6277 || id == 6276 || id == 6278) {
			n = new GodwarsBandosFaction(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 6261 || id == 6263 || id == 6265) {
			n = GodWarsBosses.graardorMinions[(id - 6261) / 2] = new GodWarMinion(
					id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		} else if (id == 6260) {
			n = new GeneralGraardor(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 6222) {
			n = new KreeArra(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 6223 || id == 6225 || id == 6227) {
			n = GodWarsBosses.armadylMinions[(id - 6223) / 2] = new GodWarMinion(
					id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		} else if (id == 6203) {
			n = new KrilTstsaroth(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 6204 || id == 6206 || id == 6208) {
			n = GodWarsBosses.zamorakMinions[(id - 6204) / 2] = new GodWarMinion(
					id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		} else if (id == 50 || id == 2642) {
			n = new KingBlackDragon(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id >= 9462 && id <= 9467) {
			n = new Strykewyrm(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea);
		} else if (id == 6248 || id == 6250 || id == 6252) {
			n = GodWarsBosses.commanderMinions[(id - 6248) / 2] = new GodWarMinion(
					id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		} else if (id == 6247) {
			n = new CommanderZilyana(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 8133) {
			n = new CorporealBeast(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 13447) {
			n = ZarosGodwars.nex = new Nex(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 13451) {
			n = ZarosGodwars.fumus = new NexMinion(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 13452) {
			n = ZarosGodwars.umbra = new NexMinion(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 13453) {
			n = ZarosGodwars.cruor = new NexMinion(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 13454) {
			n = ZarosGodwars.glacies = new NexMinion(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 14256) {
			n = new Lucien(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		} else if (id == 15174) {
			n = new Pker(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		} else if (id == 3334) {
			n = new WildyWyrm(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea);
		} else if (id == 8596) {
			n = new Avatar(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		} else if (id == 11872) {
			n = new Thunder(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 3847) {
			n = new Sea(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		} else if (id == 3319) {
			n = new Tangle(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		} else if (id == 14836) {
			n = new Skoll(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		} else if (id == 4972) {
			n = new Roc(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		} else if (id == 5421) {
			n = new Tarn(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		} else if (id == 5472) {
			n = new IceKing(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 6358) {
			n = new PestQueen(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 14301) {
			n = new Glacor(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		} else if (id == 10114) {
			n = new LucienLast(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 8335) {
			n = new MercenaryMage(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 8349 || id == 8450 || id == 8451) {
			n = new TormentedDemon(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 15149) {
			n = new MasterOfFear(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else if (id == 14696) {
			n = new GanodermicBeast(id, tile, mapAreaNameHash,
					canBeAttackFromOutOfArea, spawned);
		} else {
			n = new NPC(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
					spawned);
		}
		return n;
	}

	public static NPC spawnNPC(final int id, final WorldTile tile,
			final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea) {
		return spawnNPC(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea,
				false);
	}

	/*
	 * check if the entity region changed because moved or teled then we update
	 * it
	 */
	public static void updateEntityRegion(final Entity entity) {
		if (entity.hasFinished()) {
			if (entity instanceof Player) {
				getRegion(entity.getLastRegionId()).removePlayerIndex(
						entity.getIndex());
			} else {
				getRegion(entity.getLastRegionId()).removeNPCIndex(
						entity.getIndex());
			}
			return;
		}
		final int regionId = entity.getRegionId();
		if (entity.getLastRegionId() != regionId) { // map region entity at
			// changed
			if (entity instanceof Player) {
				if (entity.getLastRegionId() > 0) {
					getRegion(entity.getLastRegionId()).removePlayerIndex(
							entity.getIndex());
				}
				final Region region = getRegion(regionId);
				region.addPlayerIndex(entity.getIndex());
				final Player player = (Player) entity;
				final int musicId = region.getMusicId();
				if (musicId != -1) {
					player.getMusicsManager().checkMusic(musicId);
				}
				player.getControllerManager().moved();
				if (player.hasStarted()) {
					checkControlersAtMove(player);
				}
			} else {
				if (entity.getLastRegionId() > 0) {
					getRegion(entity.getLastRegionId()).removeNPCIndex(
							entity.getIndex());
				}
				getRegion(regionId).addNPCIndex(entity.getIndex());
			}
			entity.checkMultiArea();
			entity.setLastRegionId(regionId);
		} else {
			if (entity instanceof Player) {
				final Player player = (Player) entity;
				player.getControllerManager().moved();
				if (player.hasStarted()) {
					checkControlersAtMove(player);
				}
			}
			entity.checkMultiArea();
		}
	}

	private static void checkControlersAtMove(final Player player) {
		if (!(player.getControllerManager().getController() instanceof RequestController)
				&& RequestController.inWarRequest(player)) {
			player.getControllerManager().startController(RequestController.class);
		} else if (DuelController.isAtDuelArena(player)) {
			player.getControllerManager().startController(DuelController.class);
		} else if (FfaZone.inArea(player)) {
			player.getControllerManager().startController(FfaZone.class);
		} else if (DwarfCannon.isAtRockCrabs(player)) {
			player.location = "Rock Crabs";
		}
	}

	/*
	 * checks clip
	 */
	public static boolean canMoveNPC(final int plane, final int x, final int y,
			final int size) {
		for (int tileX = x; tileX < x + size; tileX++) {
			for (int tileY = y; tileY < y + size; tileY++)
				if (getMask(plane, tileX, tileY) != 0)
					return false;
		}
		return true;
	}

	/*
	 * checks clip
	 */
	public static boolean isNotCliped(final int plane, final int x,
			final int y, final int size) {
		for (int tileX = x; tileX < x + size; tileX++) {
			for (int tileY = y; tileY < y + size; tileY++)
				if ((getMask(plane, tileX, tileY) & 2097152) != 0)
					return false;
		}
		return true;
	}

	public static int getMask(final int plane, final int x, final int y) {
		final WorldTile tile = new WorldTile(x, y, plane);
		final int regionId = tile.getRegionId();
		final Region region = getRegion(regionId);
		if (region == null)
			return -1;
		final int baseLocalX = x - ((regionId >> 8) * 64);
		final int baseLocalY = y - ((regionId & 0xff) * 64);
		return region.getMask(tile.getPlane(), baseLocalX, baseLocalY);
	}

	public static void setMask(final int plane, final int x, final int y,
			final int mask) {
		final WorldTile tile = new WorldTile(x, y, plane);
		final int regionId = tile.getRegionId();
		final Region region = getRegion(regionId);
		if (region == null)
			return;
		final int baseLocalX = x - ((regionId >> 8) * 64);
		final int baseLocalY = y - ((regionId & 0xff) * 64);
		region.setMask(tile.getPlane(), baseLocalX, baseLocalY, mask);
	}

	public static int getRotation(final int plane, final int x, final int y) {
		final WorldTile tile = new WorldTile(x, y, plane);
		final int regionId = tile.getRegionId();
		final Region region = getRegion(regionId);
		if (region == null)
			return 0;
		final int baseLocalX = x - ((regionId >> 8) * 64);
		final int baseLocalY = y - ((regionId & 0xff) * 64);
		return region.getRotation(tile.getPlane(), baseLocalX, baseLocalY);
	}

	private static int getClipedOnlyMask(final int plane, final int x,
			final int y) {
		final WorldTile tile = new WorldTile(x, y, plane);
		final int regionId = tile.getRegionId();
		final Region region = getRegion(regionId);
		if (region == null)
			return -1;
		final int baseLocalX = x - ((regionId >> 8) * 64);
		final int baseLocalY = y - ((regionId & 0xff) * 64);
		return region
				.getMaskClipedOnly(tile.getPlane(), baseLocalX, baseLocalY);
	}

	public static boolean checkProjectileStep(final int plane,
			final int x, final int y, final int dir, final int size) {
		final int xOffset = Utils.DIRECTION_DELTA_X[dir];
		final int yOffset = Utils.DIRECTION_DELTA_Y[dir];
		/*
		 * int rotation = getRotation(plane,x+xOffset,y+yOffset); if(rotation !=
		 * 0) { dir += rotation; if(dir >= Utils.DIRECTION_DELTA_X.length) dir =
		 * dir - (Utils.DIRECTION_DELTA_X.length-1); xOffset =
		 * Utils.DIRECTION_DELTA_X[dir]; yOffset = Utils.DIRECTION_DELTA_Y[dir];
		 * }
		 */
		if (size == 1) {
			final int mask = getClipedOnlyMask(plane, x
					+ Utils.DIRECTION_DELTA_X[dir], y
					+ Utils.DIRECTION_DELTA_Y[dir]);
			if (xOffset == -1 && yOffset == 0)
				return (mask & 0x42240000) == 0;
			if (xOffset == 1 && yOffset == 0)
				return (mask & 0x60240000) == 0;
			if (xOffset == 0 && yOffset == -1)
				return (mask & 0x40a40000) == 0;
			if (xOffset == 0 && yOffset == 1)
				return (mask & 0x48240000) == 0;
			if (xOffset == -1 && yOffset == -1)
				return (mask & 0x43a40000) == 0
				&& (getClipedOnlyMask(plane, x - 1, y) & 0x42240000) == 0
				&& (getClipedOnlyMask(plane, x, y - 1) & 0x40a40000) == 0;
			if (xOffset == 1 && yOffset == -1)
				return (mask & 0x60e40000) == 0
				&& (getClipedOnlyMask(plane, x + 1, y) & 0x60240000) == 0
				&& (getClipedOnlyMask(plane, x, y - 1) & 0x40a40000) == 0;
			if (xOffset == -1 && yOffset == 1)
				return (mask & 0x4e240000) == 0
				&& (getClipedOnlyMask(plane, x - 1, y) & 0x42240000) == 0
				&& (getClipedOnlyMask(plane, x, y + 1) & 0x48240000) == 0;
			if (xOffset == 1 && yOffset == 1)
				return (mask & 0x78240000) == 0
				&& (getClipedOnlyMask(plane, x + 1, y) & 0x60240000) == 0
				&& (getClipedOnlyMask(plane, x, y + 1) & 0x48240000) == 0;
		} else if (size == 2) {
			if (xOffset == -1 && yOffset == 0)
				return (getClipedOnlyMask(plane, x - 1, y) & 0x43a40000) == 0
						&& (getClipedOnlyMask(plane, x - 1, y + 1) & 0x4e240000) == 0;
			if (xOffset == 1 && yOffset == 0)
				return (getClipedOnlyMask(plane, x + 2, y) & 0x60e40000) == 0
						&& (getClipedOnlyMask(plane, x + 2, y + 1) & 0x78240000) == 0;
			if (xOffset == 0 && yOffset == -1)
				return (getClipedOnlyMask(plane, x, y - 1) & 0x43a40000) == 0
						&& (getClipedOnlyMask(plane, x + 1, y - 1) & 0x60e40000) == 0;
			if (xOffset == 0 && yOffset == 1)
				return (getClipedOnlyMask(plane, x, y + 2) & 0x4e240000) == 0
						&& (getClipedOnlyMask(plane, x + 1, y + 2) & 0x78240000) == 0;
			if (xOffset == -1 && yOffset == -1)
				return (getClipedOnlyMask(plane, x - 1, y) & 0x4fa40000) == 0
						&& (getClipedOnlyMask(plane, x - 1, y - 1) & 0x43a40000) == 0
						&& (getClipedOnlyMask(plane, x, y - 1) & 0x63e40000) == 0;
			if (xOffset == 1 && yOffset == -1)
				return (getClipedOnlyMask(plane, x + 1, y - 1) & 0x63e40000) == 0
						&& (getClipedOnlyMask(plane, x + 2, y - 1) & 0x60e40000) == 0
						&& (getClipedOnlyMask(plane, x + 2, y) & 0x78e40000) == 0;
			if (xOffset == -1 && yOffset == 1)
				return (getClipedOnlyMask(plane, x - 1, y + 1) & 0x4fa40000) == 0
						&& (getClipedOnlyMask(plane, x - 1, y + 1) & 0x4e240000) == 0
						&& (getClipedOnlyMask(plane, x, y + 2) & 0x7e240000) == 0;
			if (xOffset == 1 && yOffset == 1)
				return (getClipedOnlyMask(plane, x + 1, y + 2) & 0x7e240000) == 0
						&& (getClipedOnlyMask(plane, x + 2, y + 2) & 0x78240000) == 0
						&& (getClipedOnlyMask(plane, x + 1, y + 1) & 0x78e40000) == 0;
		} else {
			if (xOffset == -1 && yOffset == 0) {
				if ((getClipedOnlyMask(plane, x - 1, y) & 0x43a40000) != 0
						|| (getClipedOnlyMask(plane, x - 1, -1 + (y + size)) & 0x4e240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getClipedOnlyMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == 0) {
				if ((getClipedOnlyMask(plane, x + size, y) & 0x60e40000) != 0
						|| (getClipedOnlyMask(plane, x + size, y - (-size + 1)) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getClipedOnlyMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0)
						return false;
			} else if (xOffset == 0 && yOffset == -1) {
				if ((getClipedOnlyMask(plane, x, y - 1) & 0x43a40000) != 0
						|| (getClipedOnlyMask(plane, x + size - 1, y - 1) & 0x60e40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getClipedOnlyMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == 0 && yOffset == 1) {
				if ((getClipedOnlyMask(plane, x, y + size) & 0x4e240000) != 0
						|| (getClipedOnlyMask(plane, x + (size - 1), y + size) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getClipedOnlyMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0)
						return false;
			} else if (xOffset == -1 && yOffset == -1) {
				if ((getClipedOnlyMask(plane, x - 1, y - 1) & 0x43a40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getClipedOnlyMask(plane, x - 1, y + (-1 + sizeOffset)) & 0x4fa40000) != 0
							|| (getClipedOnlyMask(plane, sizeOffset - 1 + x,
							y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == -1) {
				if ((getClipedOnlyMask(plane, x + size, y - 1) & 0x60e40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getClipedOnlyMask(plane, x + size, sizeOffset
							+ (-1 + y)) & 0x78e40000) != 0
							|| (getClipedOnlyMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == -1 && yOffset == 1) {
				if ((getClipedOnlyMask(plane, x - 1, y + size) & 0x4e240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getClipedOnlyMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0
							|| (getClipedOnlyMask(plane, -1 + (x + sizeOffset),
							y + size) & 0x7e240000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == 1) {
				if ((getClipedOnlyMask(plane, x + size, y + size) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getClipedOnlyMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0
							|| (getClipedOnlyMask(plane, x + size, y
							+ sizeOffset) & 0x78e40000) != 0)
						return false;
			}
		}
		return true;
	}

	public static boolean checkWalkStep(final int plane, final int x,
			final int y, final int dir, final int size) {
		int xOffset = Utils.DIRECTION_DELTA_X[dir];
		int yOffset = Utils.DIRECTION_DELTA_Y[dir];
		final int rotation = getRotation(plane, x + xOffset, y + yOffset);
		if (rotation != 0) {
			for (int rotate = 0; rotate < (4 - rotation); rotate++) {
				final int fakeChunckX = xOffset;
				final int fakeChunckY = yOffset;
				xOffset = fakeChunckY;
				yOffset = 0 - fakeChunckX;
			}
		}

		if (size == 1) {
			final int mask = getMask(plane, x + Utils.DIRECTION_DELTA_X[dir], y
					+ Utils.DIRECTION_DELTA_Y[dir]);
			if (xOffset == -1 && yOffset == 0)
				return (mask & 0x42240000) == 0;
			if (xOffset == 1 && yOffset == 0)
				return (mask & 0x60240000) == 0;
			if (xOffset == 0 && yOffset == -1)
				return (mask & 0x40a40000) == 0;
			if (xOffset == 0 && yOffset == 1)
				return (mask & 0x48240000) == 0;
			if (xOffset == -1 && yOffset == -1)
				return (mask & 0x43a40000) == 0
				&& (getMask(plane, x - 1, y) & 0x42240000) == 0
				&& (getMask(plane, x, y - 1) & 0x40a40000) == 0;
			if (xOffset == 1 && yOffset == -1)
				return (mask & 0x60e40000) == 0
				&& (getMask(plane, x + 1, y) & 0x60240000) == 0
				&& (getMask(plane, x, y - 1) & 0x40a40000) == 0;
			if (xOffset == -1 && yOffset == 1)
				return (mask & 0x4e240000) == 0
				&& (getMask(plane, x - 1, y) & 0x42240000) == 0
				&& (getMask(plane, x, y + 1) & 0x48240000) == 0;
			if (xOffset == 1 && yOffset == 1)
				return (mask & 0x78240000) == 0
				&& (getMask(plane, x + 1, y) & 0x60240000) == 0
				&& (getMask(plane, x, y + 1) & 0x48240000) == 0;
		} else if (size == 2) {
			if (xOffset == -1 && yOffset == 0)
				return (getMask(plane, x - 1, y) & 0x43a40000) == 0
						&& (getMask(plane, x - 1, y + 1) & 0x4e240000) == 0;
			if (xOffset == 1 && yOffset == 0)
				return (getMask(plane, x + 2, y) & 0x60e40000) == 0
						&& (getMask(plane, x + 2, y + 1) & 0x78240000) == 0;
			if (xOffset == 0 && yOffset == -1)
				return (getMask(plane, x, y - 1) & 0x43a40000) == 0
						&& (getMask(plane, x + 1, y - 1) & 0x60e40000) == 0;
			if (xOffset == 0 && yOffset == 1)
				return (getMask(plane, x, y + 2) & 0x4e240000) == 0
						&& (getMask(plane, x + 1, y + 2) & 0x78240000) == 0;
			if (xOffset == -1 && yOffset == -1)
				return (getMask(plane, x - 1, y) & 0x4fa40000) == 0
						&& (getMask(plane, x - 1, y - 1) & 0x43a40000) == 0
						&& (getMask(plane, x, y - 1) & 0x63e40000) == 0;
			if (xOffset == 1 && yOffset == -1)
				return (getMask(plane, x + 1, y - 1) & 0x63e40000) == 0
						&& (getMask(plane, x + 2, y - 1) & 0x60e40000) == 0
						&& (getMask(plane, x + 2, y) & 0x78e40000) == 0;
			if (xOffset == -1 && yOffset == 1)
				return (getMask(plane, x - 1, y + 1) & 0x4fa40000) == 0
						&& (getMask(plane, x - 1, y + 1) & 0x4e240000) == 0
						&& (getMask(plane, x, y + 2) & 0x7e240000) == 0;
			if (xOffset == 1 && yOffset == 1)
				return (getMask(plane, x + 1, y + 2) & 0x7e240000) == 0
						&& (getMask(plane, x + 2, y + 2) & 0x78240000) == 0
						&& (getMask(plane, x + 1, y + 1) & 0x78e40000) == 0;
		} else {
			if (xOffset == -1 && yOffset == 0) {
				if ((getMask(plane, x - 1, y) & 0x43a40000) != 0
						|| (getMask(plane, x - 1, -1 + (y + size)) & 0x4e240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == 0) {
				if ((getMask(plane, x + size, y) & 0x60e40000) != 0
						|| (getMask(plane, x + size, y - (-size + 1)) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0)
						return false;
			} else if (xOffset == 0 && yOffset == -1) {
				if ((getMask(plane, x, y - 1) & 0x43a40000) != 0
						|| (getMask(plane, x + size - 1, y - 1) & 0x60e40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == 0 && yOffset == 1) {
				if ((getMask(plane, x, y + size) & 0x4e240000) != 0
						|| (getMask(plane, x + (size - 1), y + size) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0)
						return false;
			} else if (xOffset == -1 && yOffset == -1) {
				if ((getMask(plane, x - 1, y - 1) & 0x43a40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getMask(plane, x - 1, y + (-1 + sizeOffset)) & 0x4fa40000) != 0
							|| (getMask(plane, sizeOffset - 1 + x, y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == -1) {
				if ((getMask(plane, x + size, y - 1) & 0x60e40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getMask(plane, x + size, sizeOffset + (-1 + y)) & 0x78e40000) != 0
							|| (getMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == -1 && yOffset == 1) {
				if ((getMask(plane, x - 1, y + size) & 0x4e240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0
							|| (getMask(plane, -1 + (x + sizeOffset), y + size) & 0x7e240000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == 1) {
				if ((getMask(plane, x + size, y + size) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0
							|| (getMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0)
						return false;
			}
		}
		return true;
	}

	public static boolean containsPlayer(final String username) {
		for (final Player p2 : players) {
			if (p2 == null) {
				continue;
			}
			if (p2.getUsername().equals(username))
				return true;
		}
		return false;
	}

	public static Player getPlayer(final String username) {
		for (final Player player : getPlayers()) {
			if (player == null) {
				continue;
			}
			if (player.getUsername().equals(username))
				return player;
		}
		return null;
	}

	public static Player getPlayerByDisplayName(final String username) {
		final String formatedUsername = Utils
				.formatPlayerNameForDisplay(username);
		for (final Player player : getPlayers()) {
			if (player == null) {
				continue;
			}
			if (player.getUsername().equalsIgnoreCase(formatedUsername)
					|| player.getDisplayName().equalsIgnoreCase(
							formatedUsername))
				return player;
		}
		return null;
	}

	public static NPC getNPC(final int npcId) {
		for (final NPC npc : getNPCs()) {
			if (npc.getId() == npcId)
				return npc;
		}
		return null;
	}

	public static EntityList<Player> getPlayers() {
		return players;
	}

	public static EntityList<NPC> getNPCs() {
		return npcs;
	}

	public static void safeShutdown(final boolean restart, final int delay) {
		if (exiting_start != 0)
			return;
		exiting_start = Utils.currentTimeMillis();
		exiting_delay = delay;
		for (final Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.hasFinished()) {
				continue;
			}
			player.getPackets().sendSystemUpdate(delay);
		}
		CoresManager.SLOW_EXECUTOR.schedule((Runnable) () -> {
			try {
                for (final Player player : World.getPlayers()) {
                    if (player == null || !player.hasStarted()) {
                        continue;
                    }
                    player.realFinish();
                }
                IPBanFileManager.save();
                PkRankFileManager.save();
                GrandExchange.save();
                if (restart) {
					Server.getInstance().restart();
				} else {
					Server.getInstance().stop();
				}
            } catch (final Throwable e) {
                Logger.handle(e);
            }
        }, delay, TimeUnit.SECONDS);
	}

	/*
	 * by default doesnt changeClipData
	 */
	public static void spawnTemporaryObject(final WorldObject object,
			final long time) {
		spawnTemporaryObject(object, time, false);
	}

	public static void spawnTemporaryObject(final WorldObject object,
			final long time, final boolean clip) {
		final int regionId = object.getRegionId();
		final WorldObject realMapObject = getRegion(regionId).getRealObject(
				object);
		// remakes object, has to be done because on static region coords arent
		// same of real
		final WorldObject realObject = realMapObject == null ? null
				: new WorldObject(realMapObject.getId(),
						realMapObject.getType(), realMapObject.getRotation(),
						object.getX(), object.getY(), object.getPlane());
		spawnObject(object, clip);
		final int baseLocalX = object.getX() - ((regionId >> 8) * 64);
		final int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
		if (realObject != null && clip) {
			getRegion(regionId).removeMapObject(realObject, baseLocalX,
					baseLocalY);
		}
		CoresManager.SLOW_EXECUTOR.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					getRegion(regionId).removeObject(object);
					if (clip) {
						getRegion(regionId).removeMapObject(object, baseLocalX,
								baseLocalY);
						if (realObject != null) {
							final int baseLocalX = object.getX()
									- ((regionId >> 8) * 64);
							final int baseLocalY = object.getY()
									- ((regionId & 0xff) * 64);
							getRegion(regionId).addMapObject(realObject,
									baseLocalX, baseLocalY);
						}
					}
					for (final Player p2 : players) {
						if (p2 == null || !p2.hasStarted() || p2.hasFinished()
								|| !p2.getMapRegionsIds().contains(regionId)) {
							continue;
						}
						if (realObject != null) {
							p2.getPackets().sendSpawnedObject(realObject);
						} else {
							p2.getPackets().sendDestroyObject(object);
						}
					}
				} catch (final Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);
	}

	public static boolean isSpawnedObject(final WorldObject object) {
		final int regionId = object.getRegionId();
		final WorldObject spawnedObject = getRegion(regionId).getSpawnedObject(
				object);
		return spawnedObject != null && object.getId() == spawnedObject.getId();
	}

	public static boolean removeTemporaryObject(final WorldObject object,
												final long time, final boolean clip) {
		final int regionId = object.getRegionId();
		// remakes object, has to be done because on static region coords arent
		// same of real
		final WorldObject realObject = object == null ? null : new WorldObject(
				object.getId(), object.getType(), object.getRotation(),
				object.getX(), object.getY(), object.getPlane());
		if (realObject == null)
			return false;
		removeObject(object, clip);
		CoresManager.SLOW_EXECUTOR.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					getRegion(regionId).removeRemovedObject(object);
					if (clip) {
						final int baseLocalX = object.getX()
								- ((regionId >> 8) * 64);
						final int baseLocalY = object.getY()
								- ((regionId & 0xff) * 64);
						getRegion(regionId).addMapObject(realObject,
								baseLocalX, baseLocalY);
					}
					for (final Player p2 : players) {
						if (p2 == null || !p2.hasStarted() || p2.hasFinished()
								|| !p2.getMapRegionsIds().contains(regionId)) {
							continue;
						}
						p2.getPackets().sendSpawnedObject(realObject);
					}
				} catch (final Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);

		return true;
	}

	public static void removeObject(final WorldObject object,
									final boolean clip) {
		final int regionId = object.getRegionId();
		getRegion(regionId).addRemovedObject(object);
		if (clip) {
			final int baseLocalX = object.getX() - ((regionId >> 8) * 64);
			final int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
			getRegion(regionId).removeMapObject(object, baseLocalX, baseLocalY);
		}
		synchronized (players) {
			for (final Player p2 : players) {
				if (p2 == null || !p2.hasStarted() || p2.hasFinished()
						|| !p2.getMapRegionsIds().contains(regionId)) {
					continue;
				}
				p2.getPackets().sendDestroyObject(object);
			}
		}
	}

	public static WorldObject getObject(final WorldTile tile) {
		final int regionId = tile.getRegionId();
		final int baseLocalX = tile.getX() - ((regionId >> 8) * 64);
		final int baseLocalY = tile.getY() - ((regionId & 0xff) * 64);
		return getRegion(regionId).getObject(tile.getPlane(), baseLocalX,
				baseLocalY);
	}

	public static WorldObject getObject(final WorldTile tile,
										final int type) {
		final int regionId = tile.getRegionId();
		final int baseLocalX = tile.getX() - ((regionId >> 8) * 64);
		final int baseLocalY = tile.getY() - ((regionId & 0xff) * 64);
		return getRegion(regionId).getObject(tile.getPlane(), baseLocalX,
				baseLocalY, type);
	}

	public static void spawnObject(final WorldObject object,
								   final boolean clip) {
		final int regionId = object.getRegionId();
		getRegion(regionId).addObject(object);
		object.spawnedByEd = true;
		if (clip) {
			final int baseLocalX = object.getX() - ((regionId >> 8) * 64);
			final int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
			getRegion(regionId).addMapObject(object, baseLocalX, baseLocalY);
		}
		synchronized (players) {
			for (final Player p2 : players) {
				if (p2 == null || !p2.hasStarted() || p2.hasFinished()
						|| !p2.getMapRegionsIds().contains(regionId)) {
					continue;
				}
				p2.getPackets().sendSpawnedObject(object);
			}
		}
	}

	public static void spawnTemporaryObject(final WorldObject object,
											final long time, final boolean clip, final WorldObject realObj) {
		final int regionId = object.getRegionId();
		final WorldObject realMapObject = getRegion(regionId).getRealObject(
				object);
		final WorldObject realObject = realMapObject == null ? null
				: new WorldObject(realMapObject.getId(),
						realMapObject.getType(), realMapObject.getRotation(),
						object.getX(), object.getY(), object.getPlane());
		spawnObject(object, clip);
		final int baseLocalX = object.getX() - ((regionId >> 8) * 64);
		final int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
		if (realObject != null && clip) {
			getRegion(regionId).removeMapObject(realObject, baseLocalX,
					baseLocalY);
		}
		CoresManager.SLOW_EXECUTOR.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					getRegion(regionId).removeObject(object);
					if (clip) {
						getRegion(regionId).removeMapObject(object, baseLocalX,
								baseLocalY);
						if (realObject != null) {
							final int baseLocalX = object.getX()
									- ((regionId >> 8) * 64);
							final int baseLocalY = object.getY()
									- ((regionId & 0xff) * 64);
							getRegion(regionId).addMapObject(realObject,
									baseLocalX, baseLocalY);
						}
					}
					for (final Player p2 : players) {
						if (p2 == null || !p2.hasStarted() || p2.hasFinished()
								|| !p2.getMapRegionsIds().contains(regionId)) {
							continue;
						}
						if (realObject != null) {
							p2.getPackets().sendSpawnedObject(realObject);
						} else if (object.spawnedByEd) {
							p2.getPackets().sendSpawnedObject(realObj);
						} else {
							p2.getPackets().sendDestroyObject(object);
						}
					}
				} catch (final Throwable e) {
					Logger.handle(e);
				}
			}

		}, time, TimeUnit.MILLISECONDS);
	}

	public static void addGroundItem(final Item item, final WorldTile tile) {
		final FloorItem floorItem = new FloorItem(item, tile, null, false,
				false);
		final Region region = getRegion(tile.getRegionId());
		region.forceGetFloorItems().add(floorItem);
		final int regionId = tile.getRegionId();
		for (final Player player : players) {
			if (player == null || !player.hasStarted() || player.hasFinished()
					|| player.getPlane() != tile.getPlane()
					|| !player.getMapRegionsIds().contains(regionId)) {
				continue;
			}
			player.getPackets().sendGroundItem(floorItem);
		}
	}

	public static void addGroundItem(final Item item,
			final WorldTile tile, final Player owner/* null for default */,
			final boolean underGrave,
			final long hiddenTime/* default 3minutes */, final boolean invisible) {
		addGroundItem(item, tile, owner, underGrave, hiddenTime, invisible,
				false, 180);
	}

	public static void addGroundItem(final Item item,
			final WorldTile tile, final Player owner/* null for default */,
			final boolean underGrave,
			final long hiddenTime/* default 3minutes */,
			final boolean invisible, final boolean intoGold) {
		addGroundItem(item, tile, owner, underGrave, hiddenTime, invisible,
				intoGold, 180);
	}

    public static void addGroundItem(final Item item, final WorldTile tile, final Player owner, boolean invisible, long hiddenTime) {
        addGroundItem(item, tile, owner, false, hiddenTime, invisible, false, 60);
    }

	public static void addGroundItem(final Item item,
			final WorldTile tile, final Player owner/* null for default */,
			final boolean underGrave,
			final long hiddenTime/* default 3minutes */,
			final boolean invisible, final boolean intoGold,
			final int publicTime) {
		if (intoGold) {
			if (!ItemConstants.isTradeable(item)) {
				final int price = item.getDefinitions().getGEPrice();
				if (price <= 0)
					return;
				item.setId(995);
				item.setAmount(price);
			}
		}
		final FloorItem floorItem = new FloorItem(item, tile, owner,
				owner != null && underGrave, invisible);
		final Region region = getRegion(tile.getRegionId());
		region.forceGetFloorItems().add(floorItem);
		if (invisible && hiddenTime != -1) {
			if (owner != null) {
				owner.getPackets().sendGroundItem(floorItem);
			}
			CoresManager.SLOW_EXECUTOR.schedule((Runnable) () -> {
				try {
                    if (!region.forceGetFloorItems().contains(floorItem))
                        return;
                    final int regionId = tile.getRegionId();
                    if (underGrave || !ItemConstants.isTradeable(floorItem)
                            || item.getName().contains("Dr nabanik")) {
                        region.forceGetFloorItems().remove(floorItem);
                        if (owner != null) {
                            if (owner.getMapRegionsIds().contains(regionId)
                                    && owner.getPlane() == tile.getPlane()) {
                                owner.getPackets().sendRemoveGroundItem(
                                        floorItem);
                            }
                        }
                        return;
                    }

                    floorItem.setInvisible(false);
                    for (final Player player : players) {
                        if (player == null
                                || player == owner
                                || !player.hasStarted()
                                || player.hasFinished()
                                || player.getPlane() != tile.getPlane()
                                || !player.getMapRegionsIds().contains(
                                        regionId)) {
                            continue;
                        }
                        player.getPackets().sendGroundItem(floorItem);
                    }
                    removeGroundItem(floorItem, publicTime);
                } catch (final Throwable e) {
                    Logger.handle(e);
                }
            }, hiddenTime, TimeUnit.SECONDS);
			return;
		}
		final int regionId = tile.getRegionId();
		for (final Player player : players) {
			if (player == null || !player.hasStarted() || player.hasFinished()
					|| player.getPlane() != tile.getPlane()
					|| !player.getMapRegionsIds().contains(regionId)) {
				continue;
			}
			player.getPackets().sendGroundItem(floorItem);
		}
		removeGroundItem(floorItem, publicTime);
	}

	public static void updateGroundItem(final Item item,
			final WorldTile tile, final Player owner) {
		final FloorItem floorItem = World.getRegion(tile.getRegionId())
				.getGroundItem(item.getId(), tile, owner);
		if (floorItem == null) {
			addGroundItem(item, tile, owner, false, 360, true);
			return;
		}
		floorItem.setAmount(floorItem.getAmount() + item.getAmount());
		owner.getPackets().sendRemoveGroundItem(floorItem);
		owner.getPackets().sendGroundItem(floorItem);

	}

    public static void updateGroundItem(Item item, final WorldTile tile, final Player owner, final int time) {
        final FloorItem floorItem = World.getRegion(tile.getRegionId())
                .getGroundItem(item.getId(), tile, owner);
        if (floorItem == null) {
            addGroundItem(item, tile, owner, true, time);
            return;
        }
        if (floorItem.getDefinitions().isStackable()) {
            if (floorItem.getAmount() + item.getAmount() < 0) {
                int totalAmount = Integer.MAX_VALUE - floorItem.getAmount();
                floorItem.setAmount(Integer.MAX_VALUE);
                item.setAmount(item.getAmount() - totalAmount);
                addGroundItem(item, tile, owner, true, time);
                owner.getPackets().sendRemoveGroundItem(floorItem);
                owner.getPackets().sendGroundItem(floorItem);
            } else
                floorItem.setAmount(floorItem.getAmount() + item.getAmount());
            owner.getPackets().sendRemoveGroundItem(floorItem);
            owner.getPackets().sendGroundItem(floorItem);
        } else {
            addGroundItem(item, tile, owner, true, time);
        }
    }

	private static void removeGroundItem(final FloorItem floorItem,
			final long publicTime) {
		if (publicTime < 0)
			return;
		CoresManager.SLOW_EXECUTOR.schedule(() -> {
			try {
                final int regionId = floorItem.getTile().getRegionId();
                final Region region = getRegion(regionId);
                if (!region.forceGetFloorItems().contains(floorItem))
                    return;
                region.forceGetFloorItems().remove(floorItem);
                for (final Player player : World.getPlayers()) {
                    if (player == null
                            || !player.hasStarted()
                            || player.hasFinished()
                            || player.getPlane() != floorItem.getTile()
                                    .getPlane()
                            || !player.getMapRegionsIds()
                                    .contains(regionId)) {
                        continue;
                    }
                    player.getPackets().sendRemoveGroundItem(floorItem);
                }
            } catch (final Throwable e) {
                Logger.handle(e);
            }
        }, publicTime, TimeUnit.SECONDS);
	}

	public static boolean removeGroundItem(final Player player,
										   final FloorItem floorItem) {
		return removeGroundItem(player, floorItem, true);
	}

	public static boolean removeGroundItem(final Player player,
										   final FloorItem floorItem, final boolean add) {
		final int regionId = floorItem.getTile().getRegionId();
		final Region region = getRegion(regionId);
		if (!region.forceGetFloorItems().contains(floorItem))
			return false;
		if (player.getInventory().getFreeSlots() == 0)
			return false;
		final int amount = player.getInventory().getItems().getNumberOf(995);
		if (amount < 0) {
			player.getPackets()
					.sendGameMessage(
							"You have to much coins in your Inventory, please get more space.");
			return false;
		}
		region.forceGetFloorItems().remove(floorItem);
		if (add) {
			player.getInventory().addItem(floorItem.getId(),
					floorItem.getAmount());
		}
		if (floorItem.isInvisible() || floorItem.isGrave()) {
			player.getPackets().sendRemoveGroundItem(floorItem);
			return true;
		} else {
			for (final Player p2 : World.getPlayers()) {
				if (p2 == null || !p2.hasStarted() || p2.hasFinished()
						|| p2.getPlane() != floorItem.getTile().getPlane()
						|| !p2.getMapRegionsIds().contains(regionId)) {
					continue;
				}
				p2.getPackets().sendRemoveGroundItem(floorItem);
			}
			return true;
		}
	}

	public static void sendObjectAnimation(final WorldObject object,
										   final Animation animation) {
		sendObjectAnimation(null, object, animation);
	}

	public static void sendObjectAnimation(final Entity creator,
										   final WorldObject object, final Animation animation) {
		if (creator == null) {
			for (final Player player : World.getPlayers()) {
				if (player == null || !player.hasStarted()
						|| player.hasFinished()
						|| !player.withinDistance(object)) {
					continue;
				}
				player.getPackets().sendObjectAnimation(object, animation);
			}
		} else {
			for (final int regionId : creator.getMapRegionsIds()) {
				final List<Integer> playersIndexes = getRegion(regionId)
						.getPlayerIndexes();
				if (playersIndexes == null) {
					continue;
				}
				for (final Integer playerIndex : playersIndexes) {
					final Player player = players.get(playerIndex);
					if (player == null || !player.hasStarted()
							|| player.hasFinished()
							|| !player.withinDistance(object)) {
						continue;
					}
					player.getPackets().sendObjectAnimation(object, animation);
				}
			}
		}
	}

	public static void sendGraphics(final Entity creator,
									final Graphics graphics, final WorldTile tile) {
		if (creator == null) {
			for (final Player player : World.getPlayers()) {
				if (player == null || !player.hasStarted()
						|| player.hasFinished() || !player.withinDistance(tile)) {
					continue;
				}
				player.getPackets().sendGraphics(graphics, tile);
			}
		} else {
			for (final int regionId : creator.getMapRegionsIds()) {
				final List<Integer> playersIndexes = getRegion(regionId)
						.getPlayerIndexes();
				if (playersIndexes == null) {
					continue;
				}
				for (final Integer playerIndex : playersIndexes) {
					final Player player = players.get(playerIndex);
					if (player == null || !player.hasStarted()
							|| player.hasFinished()
							|| !player.withinDistance(tile)) {
						continue;
					}
					player.getPackets().sendGraphics(graphics, tile);
				}
			}
		}
	}

	public static void sendProjectile(final Entity shooter,
									  final WorldTile startTile, final WorldTile receiver,
									  final int gfxId, final int startHeight, final int endHeight,
									  final int speed, final int delay, final int curve,
									  final int startDistanceOffset) {
		for (final int regionId : shooter.getMapRegionsIds()) {
			final List<Integer> playersIndexes = getRegion(regionId)
					.getPlayerIndexes();
			if (playersIndexes == null) {
				continue;
			}
			for (final Integer playerIndex : playersIndexes) {
				final Player player = players.get(playerIndex);
				if (player == null
						|| !player.hasStarted()
						|| player.hasFinished()
						|| (!player.withinDistance(shooter) && !player
								.withinDistance(receiver))) {
					continue;
				}
				player.getPackets().sendProjectile(null, startTile, receiver,
						gfxId, startHeight, endHeight, speed, delay, curve,
						startDistanceOffset, 1);
			}
		}
	}

	public static void sendProjectile(final WorldTile shooter,
									  final Entity receiver, final int gfxId, final int startHeight,
									  final int endHeight, final int speed, final int delay,
									  final int curve, final int startDistanceOffset) {
		for (final int regionId : receiver.getMapRegionsIds()) {
			final List<Integer> playersIndexes = getRegion(regionId)
					.getPlayerIndexes();
			if (playersIndexes == null) {
				continue;
			}
			for (final Integer playerIndex : playersIndexes) {
				final Player player = players.get(playerIndex);
				if (player == null
						|| !player.hasStarted()
						|| player.hasFinished()
						|| (!player.withinDistance(shooter) && !player
								.withinDistance(receiver))) {
					continue;
				}
				player.getPackets().sendProjectile(null, shooter, receiver,
						gfxId, startHeight, endHeight, speed, delay, curve,
						startDistanceOffset, 1);
			}
		}
	}

	public static void sendProjectile(final Entity shooter,
									  final WorldTile receiver, final int gfxId, final int startHeight,
									  final int endHeight, final int speed, final int delay,
									  final int curve, final int startDistanceOffset) {
		for (final int regionId : shooter.getMapRegionsIds()) {
			final List<Integer> playersIndexes = getRegion(regionId)
					.getPlayerIndexes();
			if (playersIndexes == null) {
				continue;
			}
			for (final Integer playerIndex : playersIndexes) {
				final Player player = players.get(playerIndex);
				if (player == null
						|| !player.hasStarted()
						|| player.hasFinished()
						|| (!player.withinDistance(shooter) && !player
								.withinDistance(receiver))) {
					continue;
				}
				player.getPackets().sendProjectile(null, shooter, receiver,
						gfxId, startHeight, endHeight, speed, delay, curve,
						startDistanceOffset, shooter.getSize());
			}
		}
	}

	public static void sendProjectile(final Entity shooter,
									  final Entity receiver, final int gfxId, final int startHeight,
									  final int endHeight, final int speed, final int delay,
									  final int curve, final int startDistanceOffset) {
		for (final int regionId : shooter.getMapRegionsIds()) {
			final List<Integer> playersIndexes = getRegion(regionId)
					.getPlayerIndexes();
			if (playersIndexes == null) {
				continue;
			}
			for (final Integer playerIndex : playersIndexes) {
				final Player player = players.get(playerIndex);
				if (player == null
						|| !player.hasStarted()
						|| player.hasFinished()
						|| (!player.withinDistance(shooter) && !player
								.withinDistance(receiver))) {
					continue;
				}
				final int size = shooter.getSize();
				player.getPackets().sendProjectile(
						receiver,
						new WorldTile(shooter.getCoordFaceX(size), shooter
								.getCoordFaceY(size), shooter.getPlane()),
						receiver, gfxId, startHeight, endHeight, speed, delay,
						curve, startDistanceOffset, size);
			}
		}
	}

	public static boolean isMultiArea(final WorldTile tile) {
		final int destX = tile.getX();
		final int destY = tile.getY();
		return (destX >= 3462 && destX <= 3511 && destY >= 9481
				&& destY <= 9521 && tile.getPlane() == 0) // kalphite queen lair
				|| (destX >= 4540 && destX <= 4799 && destY >= 5052
						&& destY <= 5183 && tile.getPlane() == 0) // thzaar city
				|| (destX >= 1721 && destX <= 1791 && destY >= 5123 && destY <= 5249) // mole
				|| (destX >= 2710 && destX <= 3667 && destY >= 2722 && destY <= 3689) // hati
				|| (destX >= 2882 && destX <= 2956 && destY >= 4421 && destY <= 4475) // dags
				|| (destX >= 3029 && destX <= 3374 && destY >= 3759 && destY <= 3903)// wild
				|| (destX >= 2250 && destX <= 2280 && destY >= 4670 && destY <= 4720)
				|| (destX >= 2511 && destX <= 2493 && destY >= 3108 && destY <= 3086) // ogres
				|| (destX >= 3337 && destX <= 3365 && destY >= 3684 && destY <= 3718) // wildy
																						// wyrm
				|| (destX >= 2892 && destX <= 2944 && destY >= 3861 && destY <= 3926) // new
																						// corp
																						// zone
																						// against
																						// safespotting
				|| (destX >= 3198 && destX <= 3380 && destY >= 3904 && destY <= 3970)
				|| (destX >= 3137 && destX <= 3164 && destY >= 5462 && destY <= 5486) // mummy
				|| (destX >= 3197 && destX <= 3209 && destY >= 5495 && destY <= 5481) // tarn
				|| (destX >= 3191 && destX <= 3326 && destY >= 3510 && destY <= 3759)
				|| (destX >= 2987 && destX <= 3006 && destY >= 3912 && destY <= 3937)
				|| (destX >= 3168 && destX <= 3188 && destY >= 5460 && destY <= 5489) // monkey
				|| (destX >= 2905 && destX <= 2988 && destY >= 4358 && destY <= 4428) // corp
																						// new
																						// 2014
				|| (destX >= 3047 && destX <= 3068 && destY >= 9569 && destY <= 9585) // ice
																						// king
				|| (destX >= 2245 && destX <= 2295 && destY >= 4675 && destY <= 4720)
				|| (destX >= 2450 && destX <= 3520 && destY >= 9450 && destY <= 9550)
				|| (destX >= 2833 && destX <= 2881 && destY >= 9616 && destY <= 9660)
				|| (destX >= 193 && destX <= 206 && destY >= 4993 && destY <= 5006) // dung
																					// boss
				|| (destX >= 3006 && destX <= 3071 && destY >= 3602 && destY <= 3710)
				|| (destX >= 2006 && destX <= 2030 && destY >= 4819 && destY <= 4833)
				|| (destX >= 3134 && destX <= 3192 && destY >= 3519 && destY <= 3646)
				|| (destX >= 2852 && destX <= 2876 && destY >= 5507 && destY <= 5547) // white
																						// portal
																						// mutli
																						// square
				|| (destX >= 1959 && destX <= 1978 && destY >= 3240 && destY <= 3262)
				|| (destX >= 2815 && destX <= 2966 && destY >= 5240 && destY <= 5375)// wild
				|| (destX >= 2840 && destX <= 2950 && destY >= 5190 && destY <= 5230) // godwars
				|| (destX >= 1490 && destX <= 1515 && destY >= 4696 && destY <= 4714) // chaos
																						// dwarf
																						// battlefield
				|| (destX >= 3547 && destX <= 3555 && destY >= 9690 && destY <= 9699) // zaros
				// godwars
				|| KingBlackDragon.atKBD(tile) // King Black Dragon lair
				|| TormentedDemon.atTD(tile) // Tormented demon's area
				|| Bork.atBork(tile) // Bork's area
				|| (destX >= 2970 && destX <= 3000 && destY >= 4365 && destY <= 4400)// corp
				|| (destX >= 3195 && destX <= 3327 && destY >= 3520
						&& destY <= 3970 || (destX >= 2376 && 5127 >= destY
						&& destX <= 2422 && 5168 <= destY))
				|| (destX >= 2374 && destY >= 5129 && destX <= 2424 && destY <= 5168) // pits
				|| (destX >= 2622 && destY >= 5696 && destX <= 2573 && destY <= 5752) // torms
				|| (destX >= 2368 && destY >= 3072 && destX <= 2431 && destY <= 3135) // castlewars
				// out
				|| (destX >= 2365 && destY >= 9470 && destX <= 2436 && destY <= 9532) // castlewars
				|| (destX >= 2948 && destY >= 5537 && destX <= 3071 && destY <= 5631) // Risk
				// ffa.
				|| (destX >= 2756 && destY >= 5537 && destX <= 2879 && destY <= 5631) // Safe
																						// ffa

				|| (tile.getX() >= 3011 && tile.getX() <= 3132
						&& tile.getY() >= 10052 && tile.getY() <= 10175 && (tile
						.getY() >= 10066 || tile.getX() >= 3094)) // fortihrny
																	// dungeon
		;
		// in

		// multi
	}

	public static boolean isPvpArea(final WorldTile tile) {
		return Wilderness.isAtWild(tile);
	}

	public static void spawnStar() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 1200) {
					star = 0;
					ShootingStar.spawnRandomStar();
				}
				loop++;
			}
		}, 0, 1);
	}

	public static void removeStarSprite(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 50) {
					for (final NPC n : World.getNPCs()) {
						if (n == null || n.getId() != 8091) {
							continue;
						}
						n.sendDeath(n);
					}
				}
				loop++;
			}
		}, 0, 1);
	}

	public static void destroySpawnedObject(final WorldObject object,
			final boolean clip) {
		final int regionId = object.getRegionId();
		final int baseLocalX = object.getX() - ((regionId >> 8) * 64);
		final int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
		final WorldObject realMapObject = getRegion(regionId).getRealObject(
				object);

		World.getRegion(regionId).removeObject(object);
		if (clip) {
			World.getRegion(regionId).removeMapObject(object, baseLocalX,
					baseLocalY);
		}
		for (final Player p2 : World.getPlayers()) {
			if (p2 == null || !p2.hasStarted() || p2.hasFinished()
					|| !p2.getMapRegionsIds().contains(regionId)) {
				continue;
			}
			if (realMapObject != null) {
				p2.getPackets().sendSpawnedObject(realMapObject);
			} else {
				p2.getPackets().sendDestroyObject(object);
			}
		}
	}

	public static void destroySpawnedObject(final WorldObject object) {
		final int regionId = object.getRegionId();
		final int baseLocalX = object.getX() - ((regionId >> 8) * 64);
		final int baseLocalY = object.getY() - ((regionId & 0xff) * 64);
		World.getRegion(regionId).removeObject(object);
		World.getRegion(regionId).removeMapObject(object, baseLocalX,
				baseLocalY);
		for (final Player p2 : World.getPlayers()) {
			if (p2 == null || !p2.hasStarted() || p2.hasFinished()
					|| !p2.getMapRegionsIds().contains(regionId)) {
				continue;
			}
			p2.getPackets().sendDestroyObject(object);
		}
	}

	public static void spawnTempGroundObject(final WorldObject object,
											 final int replaceId, final long time) {
		final int regionId = object.getRegionId();
		final WorldObject realMapObject = getRegion(regionId).getRealObject(
				object);
		final WorldObject realObject = realMapObject == null ? null
				: new WorldObject(realMapObject.getId(),
						realMapObject.getType(), realMapObject.getRotation(),
						object.getX(), object.getY(), object.getPlane());
		spawnObject(object, false);
		CoresManager.SLOW_EXECUTOR.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					getRegion(regionId).removeObject(object);
					addGroundItem(new Item(replaceId), object, null, false,
							180, false);
					for (final Player p2 : players) {
						if (p2 == null || !p2.hasStarted() || p2.hasFinished()
								|| p2.getPlane() != object.getPlane()
								|| !p2.getMapRegionsIds().contains(regionId)) {
							continue;
						}
						if (realObject != null) {
							p2.getPackets().sendSpawnedObject(realObject);
						} else {
							p2.getPackets().sendDestroyObject(object);
						}
					}
				} catch (final Throwable e) {
					Logger.handle(e);
				}
			}
		}, time, TimeUnit.MILLISECONDS);
	}

	public static void sendWorldMessage(final String message,
			final boolean forStaff) {
		for (final Player p : World.getPlayers()) {
			if (p == null || !p.isRunning() || p.isYellOff()
					|| (forStaff && p.getRank() == PlayerRank.PLAYER)) {
				continue;
			}
			p.getPackets().sendGameMessage(message);
		}
	}

	public static void sendProjectile(final WorldObject object,
									  final WorldTile startTile, final WorldTile endTile,
									  final int gfxId, final int startHeight, final int endHeight,
									  final int speed, final int delay, final int curve,
									  final int startOffset) {
		for (final Player pl : players) {
			if (pl == null || !pl.withinDistance(object, 20)) {
				continue;
			}
			pl.getPackets()
					.sendProjectile(null, startTile, endTile, gfxId,
							startHeight, endHeight, speed, delay, curve,
							startOffset, 1);
		}
	}

	public static int getIdFromName(final String playerName) {
		for (final Player p : players) {
			if (p == null) {
				continue;
			}
			if (p.getUsername().equalsIgnoreCase(
					Utils.formatPlayerNameForProtocol(playerName)))
				return p.getIndex();
		}
		return 0;
	}

}
