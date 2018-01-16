package com.rs.content.actions.skills.firemaking;

import com.rs.content.actions.Action;
import com.rs.content.actions.skills.Skills;
import com.rs.content.dialogues.impl.BonfireD;
import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.*;
import com.rs.world.item.Item;
import com.rs.world.npc.others.FireSpirit;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.ArrayList;

public class Bonfire extends Action {

	private final Log log;
	private final WorldObject object;
	private int count;
	public Bonfire(final Log log, final WorldObject object) {
		this.log = log;
		this.object = object;
	}

	public static boolean addLog(final Player player, final WorldObject object,
			final Item item) {
		for (final Log log : Log.values())
			if (log.logId == item.getId()) {
				player.getActionManager().setAction(new Bonfire(log, object));
				return true;
			}
		return false;
	}

	public static void addLogs(final Player player, final WorldObject object) {

		final ArrayList<Log> possiblities = new ArrayList<Log>();
		for (final Log log : Log.values())
			if (player.getInventory().containsItem(log.logId, 1)) {
				possiblities.add(log);
			}
		final Log[] logs = possiblities.toArray(new Log[possiblities.size()]);
		if (logs.length == 0) {
			player.getPackets().sendGameMessage(
					"You do not have any logs to add to this fire.");
		} else if (logs.length == 1) {
			player.getActionManager().setAction(new Bonfire(logs[0], object));
		} else {
			player.getDialogueManager().startDialogue(BonfireD.class, logs, object);
		}
	}

	public static double getBonfireBoostMultiplier(final Player player) {
		final int fmLvl = player.getSkills().getLevel(Skills.FIREMAKING);
		if (fmLvl >= 90)
			return 1.1;
		if (fmLvl >= 80)
			return 1.09;
		if (fmLvl >= 70)
			return 1.08;
		if (fmLvl >= 60)
			return 1.07;
		if (fmLvl >= 50)
			return 1.06;
		if (fmLvl >= 40)
			return 1.05;
		if (fmLvl >= 30)
			return 1.04;
		if (fmLvl >= 20)
			return 1.03;
		if (fmLvl >= 10)
			return 1.02;
		return 1.01;

	}

	private boolean checkAll(final Player player) {
		if (!World.getRegion(object.getRegionId()).containsObject(
				object.getId(), object))
			return false;
		if (!player.getInventory().containsItem(log.logId, 1))
			return false;
		if (player.getSkills().getLevel(Skills.FIREMAKING) < log.level) {
			player.getDialogueManager().startDialogue(
					SimpleMessage.class,
					"You need level " + log.level
							+ " Fire making to add these logs to a bonfire.");
			return false;
		}
		return true;
	}

	@Override
	public boolean start(final Player player) {
		if (checkAll(player)) {
			player.getAppearance().setRenderEmote(2498);
			return true;
		}
		return false;

	}

	@Override
	public boolean process(final Player player) {
		if (checkAll(player)) {
			if (Utils.random(500) == 0) {
				new FireSpirit(new WorldTile(object, 1), player);
				player.getPackets().sendGameMessage(
						"<col=ff0000>A fire spirit emerges from the bonfire.");
			}
			return true;
		}
		return false;
	}

	@Override
	public int processWithDelay(final Player player) {
		player.getInventory().deleteItem(log.logId, 1);
		player.getSkills().addXp(Skills.FIREMAKING,
				Firemaking.increasedExperience(player, log.xp));
		player.setNextAnimation(new Animation(16703));
		player.setNextGraphics(new Graphics(log.gfxId));
		player.getPackets().sendGameMessage("You add a log to the fire.", true);
		if (count++ == 4 && player.getLastBonfire() == 0) {
			player.setLastBonfire(log.boostTime * 100);
			final int percentage = (int) (getBonfireBoostMultiplier(player) * 100 - 100);
			player.getPackets().sendGameMessage(
					"<col=00ff00>The bonfire's warmth increases your maximum health by "
							+ percentage + "%. This will last " + log.boostTime
							+ " minutes.");
			player.getEquipment().refreshConfigs(false);
		}
		return 6;
	}

	@Override
	public void stop(final Player player) {
		player.getEmotesManager().setNextEmoteEnd(2400);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setNextAnimation(new Animation(16702));
				player.getAppearance().setRenderEmote(-1);

			}

		}, 3);
	}

	public enum Log {

		LOG(1511, 3098, 1, 40, 6), OAK(1521, 3099, 15, 50, 12), WILLOW(1519,
				3101, 30, 80.5, 18), MAPLE(1517, 3100, 45, 150, 36), YEWS(1515,
				3111, 60, 200, 54), MAGIC(1513, 3135, 75, 230, 60);
		private int logId, gfxId, level, boostTime;
		private double xp;

		Log(final int logId, final int gfxId, final int level,
			final double xp, final int boostTime) {
			this.logId = logId;
			this.gfxId = gfxId;
			this.level = level;
			this.xp = xp;
			this.boostTime = boostTime;
		}

		public int getLogId() {
			return logId;
		}

	}

}
