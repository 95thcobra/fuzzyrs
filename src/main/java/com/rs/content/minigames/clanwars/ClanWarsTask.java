package com.rs.content.minigames.clanwars;

import com.rs.core.utils.Logger;
import com.rs.player.Player;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.util.concurrent.TimeUnit;

/**
 * The timer task subclass used for clan wars updating.
 * 
 * @author Emperor
 *
 */
@GameTaskType(GameTaskManager.GameTaskType.FAST)
public final class ClanWarsTask extends GameTask {

	/**
	 * The clan wars object.
	 */
	private final ClanWars clanWars;

	/**
	 * The start ticks (before the wall goes down and the war commences).
	 */
	private int startTicks = 200;

	/**
	 * If the clan war has started.
	 */
	private boolean started;

	/**
	 * The amount of time left.
	 */
	private int timeLeft;

	/**
	 * The last amount of minutes.
	 */
	private int lastMinutes = -1;

	/**
	 * The time-out counter, we use this to check if the war has expired due to
	 * inactivity.
	 */
	private int timeOut = 0;

	public ClanWarsTask(final ClanWars clanWars, ExecutionType executionType, long initialDelay, long tick, TimeUnit timeUnit) {
		super(executionType, initialDelay, tick, timeUnit);
		this.clanWars = clanWars;
		this.timeLeft = clanWars.getTimeLeft();
	}

	@Override
	public void run() {
		try {
			if (!started) {
				if (startTicks-- == 8) {
					WallHandler.dropWall(clanWars);
				} else if (startTicks == 0) {
					started = true;
					for (final Player player : clanWars.getFirstPlayers()) {
						player.getPackets().sendGlobalConfig(270, 0);
						player.getPackets().sendGlobalConfig(260, 1);
					}
					for (final Player player : clanWars.getSecondPlayers()) {
						player.getPackets().sendGlobalConfig(270, 0);
						player.getPackets().sendGlobalConfig(260, 1);
					}
					WallHandler.removeWall(clanWars);
					clanWars.updateWar();
				}
				return;
			}
			if (clanWars.getFirstPlayers().isEmpty()
					|| clanWars.getSecondPlayers().isEmpty()) {
				if (++timeOut == 1_000) { // 10 minutes until the war is
											// time-out.
					clanWars.endWar();
					cancel(true);
					return;
				}
			} else {
				timeOut = 0; // Reset time-out.
			}
			if ((timeLeft * 0.6) / 60 != lastMinutes) {
				lastMinutes = (int) Math.ceil((timeLeft * 0.6) / 60);
				for (final Player player : clanWars.getFirstPlayers()) {
					player.getPackets().sendGlobalConfig(270, lastMinutes);
				}
				for (final Player player : clanWars.getSecondPlayers()) {
					player.getPackets().sendGlobalConfig(270, lastMinutes);
				}
				for (final Player player : clanWars.getFirstViewers()) {
					player.getPackets().sendGlobalConfig(270, lastMinutes);
				}
				for (final Player player : clanWars.getSecondViewers()) {
					player.getPackets().sendGlobalConfig(270, lastMinutes);
				}
			}
			if (timeLeft-- == 0) {
				clanWars.endWar();
				cancel(true);
			}
		} catch (final Throwable e) {
			Logger.handle(e);
		}
	}

	/**
	 * Joins the war.
	 * 
	 * @param p
	 *            The player.
	 * @param firstTeam
	 *            If the player is part of the first team/viewers.
	 */
	public void refresh(final Player p, final boolean firstTeam) {
		p.getPackets().sendGlobalConfig(
				261,
				(firstTeam ? clanWars.getFirstPlayers() : clanWars
						.getSecondPlayers()).size());
		p.getPackets().sendGlobalConfig(
				262,
				(firstTeam ? clanWars.getSecondPlayers() : clanWars
						.getFirstPlayers()).size());
		p.getPackets().sendGlobalConfig(263,
				clanWars.getKills() >> (firstTeam ? 0 : 24) & 0xFFFF);
		p.getPackets().sendGlobalConfig(264,
				clanWars.getKills() >> (firstTeam ? 24 : 0) & 0xFFFF);
		p.getPackets().sendGlobalConfig(260, started ? 1 : 0);
		p.getPackets()
				.sendGlobalConfig(270, started ? lastMinutes : startTicks);
	}

	/**
	 * If the war has started.
	 * 
	 * @return {@code True} if so.
	 */
	public boolean isStarted() {
		return started;
	}

	/**
	 * Gets the time left.
	 * 
	 * @return The time left.
	 */
	public int getTimeLeft() {
		return timeLeft;
	}

	/**
	 * Checks if the clan ended due to a timeout.
	 * 
	 * @return {@code True} if so.
	 */
	public boolean isTimeOut() {
		return timeOut > 499;
	}

}