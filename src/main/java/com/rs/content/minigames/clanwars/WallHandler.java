package com.rs.content.minigames.clanwars;

import java.util.ArrayList;
import java.util.List;

import com.rs.world.Animation;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.player.Player;
import com.rs.world.World;

/**
 * Handles the spawning, animating and removing of the wall in the clan wars
 * area.
 * 
 * @author Emperor
 *
 */
public final class WallHandler {

	/**
	 * Loads the wall.
	 * 
	 * @param c
	 *            The clan wars object.
	 */
	public static void loadWall(final ClanWars c) {
		final WorldTile base = c.getBaseLocation();
		final List<WorldObject> objects = new ArrayList<WorldObject>();
		switch (c.getAreaType()) {
		case CLASSIC_AREA: // TODO: Real classic area.
			int objectOffset = 0;
			for (int x = 5; x < 54; x++) {
				objectOffset = (objectOffset + 1) % 3;
				final WorldObject object = new WorldObject(
						28174 + objectOffset, 10, 0, base.getX() + x,
						base.getY() + 64, 0);
				World.spawnObject(object, true);
				objects.add(object);
			}
			break;
		case PLATEAU:
			for (int x = 32; x < 84; x++) {
				final WorldObject object = new WorldObject(38687, 10, 0,
						base.getX() + x, base.getY() + 32, 0);
				World.spawnObject(object, true);
				objects.add(object);
			}
			break;
		case FORSAKEN_QUARRY:
			for (int x = 5; x < 25; x++) {
				final WorldObject object = new WorldObject(38685, 10, 0,
						base.getX() + x, base.getY() + 33, 0);
				World.spawnObject(object, true);
				objects.add(object);
			}
			for (int x = 31; x < 33; x++) {
				final WorldObject object = new WorldObject(38685, 10, 0,
						base.getX() + x, base.getY() + 33, 0);
				World.spawnObject(object, true);
				objects.add(object);
			}
			for (int x = 39; x < 59; x++) {
				final WorldObject object = new WorldObject(38685, 10, 0,
						base.getX() + x, base.getY() + 33, 0);
				World.spawnObject(object, true);
				objects.add(object);
			}
			break;
		case BLASTED_FOREST:
			WorldObject object = new WorldObject(38689, 10, 0, base.getX() + 4,
					base.getY() + 33, 0);
			World.spawnObject(object, true);
			objects.add(object);
			for (int x = 5; x < 46; x++) {
				if (World.getMask(0, base.getX() + x, base.getY() + 33) != 0) {
					continue;
				}
				object = new WorldObject(38689, 10, 0, base.getX() + x,
						base.getY() + 33, 0);
				World.spawnObject(object, true);
				objects.add(object);
			}
			for (int x = 55; x < 57; x++) {
				object = new WorldObject(38689, 10, 0, base.getX() + x,
						base.getY() + 33, 0);
				World.spawnObject(object, true);
				objects.add(object);
			}
			object = new WorldObject(38689, 10, 0, base.getX() + 59,
					base.getY() + 33, 0);
			World.spawnObject(object, true);
			objects.add(object);
			break;
		case TURRETS:
			for (int x = 3; x < 60; x++) {
				object = new WorldObject(38691, 10, 1, base.getX() + x,
						base.getY() + 64, 0);
				World.spawnObject(object, true);
				objects.add(object);
			}
			break;
		}
		c.setWallObjects(objects);
	}

	/**
	 * Drops the wall, so the players can attack each other.
	 * 
	 * @param clanWars
	 *            The clan wars object.
	 */
	public static void dropWall(final ClanWars clanWars) {
		final List<WorldObject> objects = clanWars.getWallObjects();
		Animation animation;
		switch (clanWars.getAreaType()) {
		case CLASSIC_AREA:
		case PLATEAU:
			animation = new Animation(10368);
			break;
		case FORSAKEN_QUARRY:
			animation = new Animation(10369);
			break;
		case BLASTED_FOREST:
			animation = new Animation(10370);
			break;
		case TURRETS:
			animation = new Animation(10371);
			break;
		default:
			return;
		}
		for (final WorldObject object : objects) {
			for (final Player p : clanWars.getFirstPlayers()) {
				p.getPackets().sendObjectAnimation(object, animation);
			}
			for (final Player p : clanWars.getSecondPlayers()) {
				p.getPackets().sendObjectAnimation(object, animation);
			}
		}
	}

	/**
	 * Removes the clan wars wall.
	 * 
	 * @param clanWars
	 *            The clan wars object.
	 */
	public static void removeWall(final ClanWars clanWars) {
		final List<WorldObject> objects = clanWars.getWallObjects();
		for (final WorldObject object : objects) {
			World.removeObject(object, true);
		}
	}
}