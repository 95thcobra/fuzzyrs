package com.rs.content.minigames.clanwars;

import com.rs.player.Player;
import com.rs.player.controlers.Controller;
import com.rs.player.controlers.Wilderness;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

/**
 * Handles the FFA Clan Wars zone.
 * 
 * @author Emperor
 *
 */
public final class FfaZone extends Controller {

	/**
	 * If the FFA zone is the risk zone.
	 */
	private boolean risk;

	/**
	 * If the player was in the ffa pvp area.
	 */
	private transient boolean wasInArea;

	/**
	 * Checks if the location is in a ffa pvp zone.
	 *
	 * @param t The worldtask tile.
	 * @return {@code True} if so.
	 */
	public static boolean inPvpArea(final WorldTile t) {
		return (t.getX() >= 2948 && t.getY() >= 5512 && t.getX() <= 3071 && t
				.getY() <= 5631) // Risk area.
				|| (t.getX() >= 2756 && t.getY() >= 5512 && t.getX() <= 2879 && t
				.getY() <= 5631); // Safe area.
	}

	/**
	 * Checks if the location is in a ffa zone.
	 *
	 * @param t The worldtask tile.
	 * @return {@code True} if so.
	 */
	public static boolean inArea(final WorldTile t) {
		return (t.getX() >= 2948 && t.getY() >= 5508 && t.getX() <= 3071 && t
				.getY() <= 5631) // Risk area.
				|| (t.getX() >= 2756 && t.getY() >= 5508 && t.getX() <= 2879 && t
				.getY() <= 5631); // Safe area.
	}

	/**
	 * Checks if a player's overload effect is changed (due to being in the risk
	 * ffa zone, in pvp)
	 *
	 * @param player The player.
	 * @return {@code True} if so.
	 */
	public static boolean isOverloadChanged(final Player player) {
		if (!(player.getControllerManager().getController() instanceof FfaZone))
			return false;
		return player.isCanPvp()
				&& ((FfaZone) player.getControllerManager().getController()).risk;
	}

	@Override
	public void start() {
		if (player.getSkills().getCombatLevel() < 30) {
			player.sendMessage("You need a combat level of at least 30 to enter.");
			return;
		}
		if (getArguments() == null || getArguments().length < 1) {
			this.risk = player.getX() >= 2948 && player.getY() >= 5508
					&& player.getX() <= 3071 && player.getY() <= 5631;
		} else {
			this.risk = (Boolean) getArguments()[0];
		}
		player.getInterfaceManager().sendTab(
				player.getInterfaceManager().hasRezizableScreen() ? 11 : 27,
				789);
		player.getAppearance().setRenderEmote(-1);
		moved();
	}

	@Override
	public boolean sendDeath() {
		player.lock(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(
							"Oh dear, you are dead!");
				} else if (loop == 3) {
					if (risk) {
						final Player killer = player
								.getMostDamageReceivedSourcePlayer();
						if (killer != null) {
							killer.removeDamage(player);
							killer.increaseKillCount(player);
							player.sendItemsOnDeath(killer);
						}
						player.getEquipment().init();
						player.getInventory().init();
					}
					player.setNextWorldTile(new WorldTile(2993, 9679, 0));
					player.getControllerManager().startController(RequestController.class);
					player.reset();
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
	public void magicTeleported(final int type) {
		player.getControllerManager().forceStop();
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		switch (object.getId()) {
		case 38700:
			player.setNextWorldTile(new WorldTile(2606, 3101, 0));
			return false;
		}
		return true;
	}

	@Override
	public void moved() {
		final boolean inArea = inPvpArea(player);
		if (inArea && !wasInArea) {
			player.getPackets().sendPlayerOption("Attack", 1, true);
			player.setCanPvp(true);
			player.getAppearance().setRenderEmote(-1);
			wasInArea = true;
			if (risk) {
				player.setWildernessSkull();
			}
			Wilderness.checkBoosts(player);
		} else if (!inArea && wasInArea) {
			player.getPackets().sendPlayerOption("null", 1, true);
			player.setCanPvp(false);
			wasInArea = false;
		}
	}

	@Override
	public boolean keepCombating(final Entity victim) {
		if (!(victim instanceof Player))
			return true;
		return player.isCanPvp() && ((Player) victim).isCanPvp();
	}

	@Override
	public void forceClose() {
		player.setCanPvp(false);
		player.getPackets().sendPlayerOption("null", 1, true);
		final boolean resized = player.getInterfaceManager()
				.hasRezizableScreen();
		player.getPackets().closeInterface(resized ? 746 : 548,
				resized ? 11 : 27);
	}

	@Override
	public boolean logout() {
		return false;
	}
}