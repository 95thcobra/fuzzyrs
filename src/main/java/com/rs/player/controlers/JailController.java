package com.rs.player.controlers;

import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

public class JailController extends Controller {

	public static void stopControler(final Player p) {
		p.getControllerManager().getController().removeControler();
	}

	@Override
	public void start() {
		if (player.getJailed() > Utils.currentTimeMillis()) {
			player.sendRandomJail(player);
		}
	}

	@Override
	public void process() {
		if (player.getJailed() <= Utils.currentTimeMillis()) {
			player.getControllerManager().getController().removeControler();
			player.getPackets().sendGameMessage(
					"Your account has been unjailed.", true);
		}
	}

	@Override
	public boolean sendDeath() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				player.stopAll();
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
					player.setNextAnimation(new Animation(-1));
					player.reset();
					player.setCanPvp(false);
					player.sendRandomJail(player);
					player.unlock();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public boolean login() {

		return false;
	}

	@Override
	public boolean logout() {

		return false;
	}

	@Override
	public boolean processMagicTeleport(final WorldTile toTile) {
		player.getPackets().sendGameMessage(
				"You are currently jailed for your delinquent acts.");
		return false;
	}

	@Override
	public boolean processItemTeleport(final WorldTile toTile) {
		player.getPackets().sendGameMessage(
				"You are currently jailed for your delinquent acts.");
		return false;
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		player.getPackets().sendGameMessage(
				"You cannot do any activities while being jailed.");
		return false;
	}

}
