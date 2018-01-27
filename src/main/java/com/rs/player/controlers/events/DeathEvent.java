package com.rs.player.controlers.events;

import com.rs.core.cores.CoresManager;
import com.rs.player.content.Magic;
import com.rs.player.controlers.Controller;
import com.rs.world.Animation;
import com.rs.world.region.RegionBuilder;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

import java.util.concurrent.TimeUnit;

public class DeathEvent extends Controller {

	public static final WorldTile RESPAWN = new WorldTile(3214, 3423, 0);

	private int[] boundChuncks;
	private Stages stage;

	@Override
	public void start() {
		loadRoom();
	}

	@Override
	public boolean login() {
		loadRoom();
		return false;
	}

	@Override
	public boolean logout() {
		player.setLocation(new WorldTile(1978, 5302, 0));
		destroyRoom();
		return false;
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().closeCombatStyles();
		player.getInterfaceManager().closeTaskSystem();
		player.getInterfaceManager().closeSkills();
		player.getInterfaceManager().closeInventory();
		player.getInterfaceManager().closeEquipment();
		player.getInterfaceManager().closePrayerBook();
		player.getInterfaceManager().closeMagicBook();
		player.getInterfaceManager().closeEmotes();
	}

	public void loadRoom() {
		stage = Stages.LOADING;
		player.lock(); // locks player
		CoresManager.SLOW_EXECUTOR.execute(() -> {
			boundChuncks = RegionBuilder.findEmptyChunkBound(2, 2);
			RegionBuilder.copyMap(246, 662, boundChuncks[0],
					boundChuncks[1], 2, 2, new int[1], new int[1]);
			player.reset();
			player.setNextWorldTile(new WorldTile(boundChuncks[0] * 8 + 10,
					boundChuncks[1] * 8 + 6, 0));
			player.setNextAnimation(new Animation(-1));
			// 1delay because player cant walk while teleing :p, + possible
			// issues avoid
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.getMusicsManager().playMusic(683);
					player.getPackets().sendBlackOut(2);
					sendInterfaces();
					player.unlock(); // unlocks player
					stage = Stages.RUNNING;
				}

			}, 1);
		});
	}

	@Override
	public boolean processMagicTeleport(final WorldTile toTile) {
		return false;
	}

	@Override
	public boolean processItemTeleport(final WorldTile toTile) {
		return false;
	}

	/**
	 * return process normaly
	 */
	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (object.getId() == 45803) {
			Magic.sendObjectTeleportSpell(player, true, RESPAWN);
			return false;
		}
		return true;
	}

	@Override
	public void magicTeleported(final int type) {
		destroyRoom();
		player.getPackets().sendBlackOut(0);
		player.getInterfaceManager().sendCombatStyles();
		player.getCombatDefinitions().sendUnlockAttackStylesButtons();
		player.getInterfaceManager().sendQuestTab();
		player.getInterfaceManager().sendSkills();
		player.getInterfaceManager().sendInventory();
		player.getInventory().unlockInventoryOptions();
		player.getInterfaceManager().sendEquipment();
		player.getInterfaceManager().sendPrayerBook();
		player.getPrayer().unlockPrayerBookButtons();
		player.getInterfaceManager().sendMagicBook();
		player.getInterfaceManager().sendEmotes();
		player.getEmotesManager().unlockEmotesBook();
		removeControler();
	}

	public void destroyRoom() {
		if (stage != Stages.RUNNING)
			return;
		stage = Stages.DESTROYING;
		CoresManager.SLOW_EXECUTOR.schedule((Runnable) () -> RegionBuilder
				.destroyMap(boundChuncks[0], boundChuncks[1], 8, 8), 1200, TimeUnit.MILLISECONDS);
	}

	@Override
	public void forceClose() {
		destroyRoom();
	}

	private enum Stages {
		LOADING, RUNNING, DESTROYING
	}

}
