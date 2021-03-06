package com.rs.player.controlers;

import com.rs.server.Server;
import com.rs.core.cores.CoresManager;
import com.rs.utils.Logger;
import com.rs.player.Player;
import com.rs.player.content.FadingScreen;
import com.rs.world.Animation;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

public class ZombieMinigame extends Controller {
	private boolean logoutAtEnd;
	private boolean login;

	@Override
	public void start() {
		fade(player);
		player.getPackets().sendGameMessage("Welcome to Daemonheim");
		player.setNextWorldTile(new WorldTile(199, 5009, 0));
		player.setForceMultiArea(true);
	}

	@Override
	public boolean processItemTeleport(final WorldTile toTile) {
		return false;
	}

	@Override
	public void sendInterfaces() {
	}

	@Override
	public boolean processButtonClick(final int interfaceId,
			final int componentId, final int slotId, final int packetId) {
		if (interfaceId == 182 && (componentId == 6 || componentId == 13)) {
			if (!logoutAtEnd) {
				logoutAtEnd = true;
				player.getPackets()
						.sendGameMessage(
								"<col=ff0000>All your progress will be deleted, proceed?");
			} else {
				removeControler();
			}
			player.forceLogout();
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (object.getId() == 500 || object.getId() == 501) {
			player.setNextWorldTile(new WorldTile(117, 5159, player.getPlane()));
			return false;
		}
		return true;
	}

	@Override
	public void magicTeleported(final int type) {
		removeControler();
	}

	public void fade(final Player player) {
		final long time = FadingScreen.fade(player);
		CoresManager.SLOW_EXECUTOR.execute(new Runnable() {
			@Override
			public void run() {
				try {
					FadingScreen.unfade(player, time, new Runnable() {
						@Override
						public void run() {

						}
					});
				} catch (final Throwable e) {
					Logger.handle(e);
				}
			}

		});
	}

	@Override
	public boolean sendDeath() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(
							"Oh dear, you are death.");
				} else if (loop == 3) {
					player.reset();
					player.setNextWorldTile(new WorldTile(
							Server.getInstance().getSettingsManager().getSettings().getDungeonPlayerLocation()));
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					player.getPackets().sendMusicEffect(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public boolean login() {
		return false; // so doesnt remove script
	}

	@Override
	public boolean logout() {
		return false; // so doesnt remove script
	}
}