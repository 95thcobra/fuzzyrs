package com.rs.content.actions.skills.construction;

import java.util.ArrayList;
import java.util.List;

import com.rs.player.Player;
import com.rs.world.DynamicRegion;
import com.rs.world.Region;
import com.rs.world.RegionBuilder;
import com.rs.world.World;
import com.rs.world.WorldObject;

public class House {

	private final ArrayList<RoomReference> rooms;
	private final byte place, look;

	public House() {
		rooms = new ArrayList<RoomReference>();
		rooms.add(new RoomReference(Room.GARDEN, 4, 4, 0, 0));
		rooms.add(new RoomReference(Room.PARLOUR, 5, 4, 0, 0));
		place = 2; // desert
		look = 5; // idk style
	}

	public void refresh(final Player player, final int[] boundChuncks) {
		player.setForceNextMapLoadRefresh(true);
		constructHouse(boundChuncks, true);
		player.loadMapRegions();
	}

	public void constructHouse(final int[] boundChuncks, final boolean buildMode) {
		final Object[][][][] data = new Object[4][8][8][];
		for (final RoomReference reference : rooms) {
			data[reference.plane][reference.x][reference.y] = new Object[] {
					reference.room.chunkX, reference.room.chunkY,
					reference.rotation, reference.room.showRoof };
		}
		if (!buildMode) { // construct roof
			for (int x = 1; x < 7; x++) {
				skipY: for (int y = 1; y < 7; y++) {
					for (int plane = 2; plane >= 0; plane--) {
						if (data[plane][x][y] != null) {
							final boolean hasRoof = (boolean) data[plane][x][y][3];
							if (hasRoof) {
								final byte rotation = (byte) data[plane][x][y][2];
								// TODO find best Roof
								data[plane + 1][x][y] = new Object[] {
										Roof.ROOF1.chunkX, Roof.ROOF1.chunkY,
										rotation, true };
								continue skipY;
							}
						}
					}
				}
			}
		}
		for (int plane = 0; plane < data.length; plane++) {
			for (int x = 0; x < data[plane].length; x++) {
				for (int y = 0; y < data[plane][x].length; y++) {
					if (data[plane][x][y] != null) {
						RegionBuilder.copyChunk(
								(int) data[plane][x][y][0]
										+ ((boolean) data[plane][x][y][3]
												&& look >= 4 ? 8 : 0),
								(int) data[plane][x][y][1],
								(boolean) data[plane][x][y][3] ? look % 4
										: place, boundChuncks[0] + x,
								boundChuncks[1] + y, plane,
								(byte) data[plane][x][y][2]);
					} else if (plane == 0) {
						RegionBuilder.copyChunk(LAND[0], LAND[1], place,
								boundChuncks[0] + x, boundChuncks[1] + y,
								plane, 0);
					}
				}
			}
		}
		final Region region = World.getRegion(RegionBuilder.getRegionHash(
				boundChuncks[0] / 8, boundChuncks[1] / 8));
		final List<WorldObject> spawnedObjects = region.getSpawnedObjects();
		if (spawnedObjects != null) {
			spawnedObjects.clear();
		}
		final List<WorldObject> removedObjects = region.getRemovedObjects();
		if (removedObjects != null) {
			removedObjects.clear();
		}
		for (final RoomReference reference : rooms) {
			final int boundX = reference.x * 8;
			final int boundY = reference.y * 8;
			final int chunkRotation = region.getRotation(reference.plane,
					boundX, boundY);
			for (int x = boundX; x < boundX + 8; x++) {
				for (int y = boundY; y < boundY + 8; y++) {
					final WorldObject[] objects = region.getObjects(
							reference.plane, x, y);
					if (objects != null) {

						for (WorldObject object : objects) {
							if (!buildMode
									&& object.getDefinitions().containsOption(
											"Build")) {
								object = new WorldObject(
										object.getId(),
										object.getType(),
										(object.getRotation() + chunkRotation) % 4,
										x + boundChuncks[0] * 8, y
												+ boundChuncks[1] * 8,
										reference.plane);
								World.removeObject(object, true);
							} else if (object.getDefinitions().name
									.equals("Window")) {
								object = new WorldObject(
										getWindowId(),
										object.getType(),
										(object.getRotation() + chunkRotation) % 4,
										x + boundChuncks[0] * 8, y
												+ boundChuncks[1] * 8,
										reference.plane);
								World.spawnObject(object, false);
							}
						}
					}
				}
			}
		}
	}

	public void previewRoom(final Player player, final int[] boundChuncks,
			final RoomReference reference, final boolean remove) {
		final int boundX = reference.x * 8;
		final int boundY = reference.y * 8;
		final Region region = World.getRegion(RegionBuilder.getRegionHash(
				reference.room.chunkX / 8, reference.room.chunkY / 8));
		final int boundX2 = (reference.room.chunkX - (reference.room.chunkX / 8 * 8)) * 8;
		final int boundY2 = (reference.room.chunkY - (reference.room.chunkY / 8 * 8)) * 8;
		final DynamicRegion house = (DynamicRegion) World
				.getRegion(RegionBuilder.getRegionHash(boundChuncks[0] / 8,
						boundChuncks[1] / 8));
		house.getRegionCoords()[reference.plane][reference.x][reference.y][3] = reference.rotation;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				final WorldObject[] objects = region.getObjects(
						reference.plane, boundX2 + x, boundY2 + y);
				if (objects != null) {
					for (WorldObject object : objects) {
						if (object.getDefinitions().containsOption(4, "Build")) {
							int x2 = x;
							int y2 = y;
							for (int rotate = 0; rotate < reference.rotation; rotate++) {
								final int fakeChunckX = x2;
								final int fakeChunckY = y2;
								x2 = fakeChunckY;
								y2 = 7 - fakeChunckX;
							}
							object = new WorldObject(
									object.getId(),
									object.getType(),
									(object.getRotation() + reference.rotation) % 4,
									boundX + x2 + boundChuncks[0] * 8, boundY
											+ y2 + boundChuncks[1] * 8,
									reference.plane);
							if (remove) {
								player.getPackets().sendDestroyObject(object);
							} else {
								player.getPackets().sendSpawnedObject(object);
							}
						}
					}
				}
			}
		}
	}

	public int getWindowId() {
		switch (look) {
		default:
			return 13117;
		}
	}

	private static final int[] LAND = { 233, 632 };

	public enum Room {
		GARDEN(232, 633, false, RegionBuilder.NORTH, RegionBuilder.EAST,
				RegionBuilder.SOUTH, RegionBuilder.WEST), PARLOUR(232, 639,
				true, RegionBuilder.EAST, RegionBuilder.SOUTH,
				RegionBuilder.WEST);

		private int chunkX, chunkY;
		private boolean showRoof;
		private int[] doorDirs;

		Room(final int chunkX, final int chunkY,
			 final boolean showRoof, final int... doorDirs) {
			this.chunkX = chunkX;
			this.chunkY = chunkY;
			this.showRoof = showRoof;
			this.doorDirs = doorDirs;
		}
	}

	public enum Roof {

		ROOF1(233, 634, RegionBuilder.NORTH, RegionBuilder.SOUTH), ROOF2(235,
				634, RegionBuilder.NORTH, RegionBuilder.EAST,
				RegionBuilder.SOUTH), ROOF3(235, 634, RegionBuilder.NORTH,
				RegionBuilder.EAST, RegionBuilder.SOUTH, RegionBuilder.NORTH);

		private int chunkX, chunkY;
		private int[] dirs;

		Roof(final int chunkX, final int chunkY, final int... dirs) {
			this.chunkX = chunkX;
			this.chunkY = chunkY;
			this.dirs = dirs;
		}
	}

	public static class RoomReference {

		public RoomReference(final Room room, final int x, final int y,
				final int plane, final int rotation) {
			this.room = room;
			this.x = (byte) x;
			this.y = (byte) y;
			this.plane = (byte) plane;
			this.rotation = (byte) rotation;
		}

		private final Room room;
		private final byte x, y, plane, rotation;

	}
}
