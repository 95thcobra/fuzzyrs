package com.rs.content.actions.skills.firemaking;

import com.rs.content.actions.Action;
import com.rs.content.actions.skills.Skills;
import com.rs.content.minigames.duel.DuelArena;
import com.rs.content.minigames.duel.DuelController;
import com.rs.server.net.handlers.inventory.InventoryOptionsHandler;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.World;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.world.item.FloorItem;
import com.rs.world.item.Item;
import com.rs.world.npc.familiar.Familiar;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

public class Firemaking extends Action {

	private final Fire fire;

	public Firemaking(final Fire fire) {
		this.fire = fire;
	}

	public static boolean isFiremaking(final Player player, final Item item1,
			final Item item2) {
		final Item log = InventoryOptionsHandler.contains(590, item1, item2);
		if (log == null)
			return false;
		return isFiremaking(player, log.getId());
	}

	public static boolean isFiremaking(final Player player, final int logId) {
		for (final Fire fire : Fire.values()) {
			if (fire.getLogId() == logId) {
				player.getActionManager().setAction(new Firemaking(fire));
				return true;
			}
		}
		return false;

	}

	public static void startFamiliarFire(final Player player,
			final Familiar familiar, final Fire fire) {
		if (player.getFamiliar().getId() == 7378
				|| player.getFamiliar().getId() == 7377) {
		}
	}

	public static double increasedExperience(final Player player, double totalXp) {
		if (player.getEquipment().getGlovesId() == 13660) {
			totalXp *= 1.025;
		}
		if (player.getEquipment().getRingId() == 13659) {
			totalXp *= 1.025;
		}
		return totalXp;
	}

	@Override
	public boolean start(final Player player) {
		if (!checkAll(player))
			return false;
		player.getPackets().sendGameMessage("You attempt to light the logs.",
				true);
		player.getInventory().deleteItem(fire.getLogId(), 1);
		World.addGroundItem(new Item(fire.getLogId(), 1),
				new WorldTile(player), player, false, 180, true);
		final Long time = (Long) player.getTemporaryAttributtes()
				.remove("Fire");
		final boolean quickFire = time != null
				&& time > Utils.currentTimeMillis();
		setActionDelay(player, quickFire ? 1 : 2);
		if (!quickFire) {
			player.setNextAnimation(new Animation(16700));
		}
		return true;
	}

	public boolean checkAll(final Player player) {
		if (!player.getInventory().containsItem(590, 1)) {
			player.getPackets().sendGameMessage(
					"You do not have the required items to light this.");
			return false;
		}
		if (player.getSkills().getLevel(Skills.FIREMAKING) < fire.getLevel()) {
			player.getPackets().sendGameMessage(
					"You do not have the required level to light this.");
			return false;
		}
		if (!World.canMoveNPC(player.getPlane(), player.getX(), player.getY(),
				1) // cliped
				|| World.getRegion(player.getRegionId()).getSpawnedObject(
						player) != null
				|| player.getControllerManager().getController() instanceof DuelArena
				|| player.getControllerManager().getController() instanceof DuelController) { // contains
			// object
			player.getPackets().sendGameMessage("You can't light a fire here.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(final Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(final Player player) {
		final WorldTile tile = new WorldTile(player);
		if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1))
			if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1))
				if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1)) {
					player.addWalkSteps(player.getX(), player.getY() - 1, 1);
				}
		player.getPackets().sendGameMessage(
				"The fire catches and the logs begin to burn.", true);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				final FloorItem item = World.getRegion(tile.getRegionId())
						.getGroundItem(fire.getLogId(), tile, player);
				if (item == null)
					return;
				if (!World.removeGroundItem(player, item, false))
					return;
				// player.getPackets().sendSound(2594, 0, 1); //TODO find fire
				// sound
				World.spawnTempGroundObject(new WorldObject(fire.getFireId(),
								10, 0, tile.getX(), tile.getY(), tile.getPlane()), 592,
						fire.getLife());
				player.getSkills().addXp(Skills.FIREMAKING, increasedExperience(player, fire.getExperience()));
				player.setNextFaceWorldTile(tile);
			}
		}, 1);
		player.getTemporaryAttributtes().put("Fire",
				Utils.currentTimeMillis() + 1800);
		return -1;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}

	public enum Fire {
		NORMAL(1511, 1, 300, 70755, 40, 20), ACHEY(2862, 1, 300, 70756, 40, 1), OAK(
				1521, 15, 450, 70757, 60, 1), WILLOW(1519, 30, 450, 70758, 90,
				1), TEAK(6333, 35, 450, 70759, 105, 1), ARCTIC_PINE(10810, 42,
				500, 70760, 125, 1), MAPLE(1517, 45, 500, 70761, 135, 1), MAHOGANY(
				6332, 50, 700, 70762, 157.5, 1), EUCALYPTUS(12581, 58, 700,
				70763, 193.5, 1), YEW(1515, 60, 800, 70764, 202.5, 1), MAGIC(
				1513, 75, 900, 70765, 303.8, 1), CURSED_MAGIC(13567, 82, 1000,
				70766, 303.8, 1);

		private int logId;
		private int level;
		private int life;
		private int fireId;
		private int time;
		private double xp;

		Fire(final int logId, final int level, final int life,
			 final int fireId, final double xp, final int time) {
			this.logId = logId;
			this.level = level;
			this.life = life;
			this.fireId = fireId;
			this.xp = xp;
			this.time = time;
		}

		public int getLogId() {
			return logId;
		}

		public int getLevel() {
			return level;
		}

		public int getLife() {
			return (life * 600);
		}

		public int getFireId() {
			return fireId;
		}

		public double getExperience() {
			return xp;
		}

		public int getTime() {
			return time;
		}
	}

}
