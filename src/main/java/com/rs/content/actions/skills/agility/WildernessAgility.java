package com.rs.content.actions.skills.agility;

import com.rs.content.actions.skills.Skills;
import com.rs.player.Player;
import com.rs.world.*;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

public class WildernessAgility {

	/*
	 * Author Bandoswhips
	 */

	public static void GateWalk(final Player player) {
		if (player.getX() != 2998 || player.getY() != 3915) {
			player.addWalkSteps(2998, 3915, -1, false);
		}
		final boolean running = player.getRun();
		player.setRunHidden(false);
		player.lock(8);
		player.addWalkSteps(2998, 3931, -1, false);
		player.getPackets().sendGameMessage(
				"You walk carefully upwards to the Agility Arena");
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearance().setRenderEmote(155);
				} else {
					player.getAppearance().setRenderEmote(-1);
					player.setRunHidden(running);
					stop();
				}
			}
		}, 0, 6);
	}

	public static void GateWalkBack(final Player player) {
		if (player.getX() != 2998 || player.getY() != 3931) {
			player.addWalkSteps(2998, 3931, -1, false);
		}
		final boolean running = player.getRun();
		player.setRunHidden(false);
		player.lock(8);
		player.addWalkSteps(2998, 3915, -1, false);
		player.getPackets().sendGameMessage(
				"You walk back to the Dangerous Wilderness");
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearance().setRenderEmote(155);
				} else {
					player.getAppearance().setRenderEmote(-1);
					player.setRunHidden(running);
					stop();
				}
			}
		}, 0, 6);
	}

	public static void enterObstaclePipe(final Player player,
			final int objectX, final int objectY) {
		if (!Agility.hasLevel(player, 35))
			return;
		final boolean running = player.getRun();
		player.setRunHidden(false);
		player.lock(8);
		player.addWalkSteps(objectX, objectY == 3938 ? 3950 : 3937, -1, false);
		player.getPackets().sendGameMessage(
				"You pulled yourself through the pipes.", true);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearance().setRenderEmote(295);
				} else {
					player.getAppearance().setRenderEmote(-1);
					setWildernessStage(player, 0);
					player.setRunHidden(running);
					player.getSkills().addXp(Skills.AGILITY, 250);
					stop();
				}
			}
		}, 0, 6);
	}

	public static void swingOnRopeSwing(final Player player,
			final WorldObject object) {
		if (!Agility.hasLevel(player, 35))
			return;
		if (player.getY() != 3953 && player.getX() != 3005) {
			player.addWalkSteps(3005, 3953, -1, false);
		}
		player.lock(4);
		player.setNextAnimation(new Animation(751));
		World.sendObjectAnimation(player, object, new Animation(497));
		final WorldTile toTile = new WorldTile(object.getX(), 3958,
				object.getPlane());
		player.setNextForceMovement(new ForceMovement(player, 1, toTile, 3,
				ForceMovement.NORTH));
		player.getSkills().addXp(Skills.AGILITY, 22);
		player.getPackets()
				.sendGameMessage("You skilfully swing across.", true);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setNextWorldTile(toTile);
				if (getWildernessStage(player) == 0) {
					setWildernessStage(player, 1);
				}
				player.getSkills().addXp(Skills.AGILITY, 500);
			}

		}, 1);
	}

	/*
	 * Stepping Stone Method by Cjay0091
	 */
	public static void crossSteppingPalletes(final Player player,
			final WorldObject object) {
		if (player.getY() != object.getY())
			return;
		player.lock(6);
		WorldTasksManager.schedule(new WorldTask() {

			int x;

			@Override
			public void run() {
				if (x++ == 6) {
					stop();
					return;
				}
				final WorldTile toTile = new WorldTile(3002 - x, player.getY(),
						player.getPlane());
				player.setNextForceMovement(new ForceMovement(toTile, 1,
						ForceMovement.WEST));
				player.setNextAnimation(new Animation(741));
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						player.setNextWorldTile(toTile);
					}
				}, 0);
			}
		}, 2, 1);
		player.getSkills().addXp(Skills.AGILITY, 600);
		if (getWildernessStage(player) == 1) {
			setWildernessStage(player, 2);
		}
	}

	public static void walkLog(final Player player) {
		if (player.getX() != 3002 || player.getY() != 3945) {
			player.addWalkSteps(3002, 3945, -1, false);
		}
		final boolean running = player.getRun();
		player.setRunHidden(false);
		player.lock(8);
		player.addWalkSteps(2994, 3945, -1, false);
		player.getPackets().sendGameMessage(
				"You walk carefully across the log...", true);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearance().setRenderEmote(155);
				} else {
					player.getAppearance().setRenderEmote(-1);
					player.setRunHidden(running);
					player.getSkills().addXp(Skills.AGILITY, 7.5);
					player.getPackets().sendGameMessage(
							"... and make it safely to the other side.", true);
					stop();
					if (getWildernessStage(player) == 2) {
						setWildernessStage(player, 850);
					}
				}
			}
		}, 0, 6);
	}

	public static void climbCliff(final Player player, final WorldObject object) {
		if (player.getY() != 3939)
			return;
		player.setNextAnimation(new Animation(3378));
		final WorldTile toTile = new WorldTile(player.getX(), 3935, 0);

		player.getPackets().sendGameMessage("You climb up the rock.", true);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setNextWorldTile(toTile);
				player.setNextAnimation(new Animation(-1));
				player.getAppearance().setRenderEmote(-1);
				stop();
				if (getWildernessStage(player) == 3) {
					removeWildernessStage(player);
					player.getSkills().addXp(Skills.AGILITY, 1000);

				}
			}
		}, 6);
	}

	public static void removeWildernessStage(final Player player) {
		player.getTemporaryAttributtes().remove("WildernessCourse");
	}

	public static void setWildernessStage(final Player player, final int stage) {
		player.getTemporaryAttributtes().put("WildernessCourse", stage);
	}

	public static int getWildernessStage(final Player player) {
		final Integer stage = (Integer) player.getTemporaryAttributtes().get(
				"WildernessCourse");
		if (stage == null)
			return -1;
		return stage;
	}
}