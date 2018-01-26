package com.rs.world;

import com.rs.Server;
import com.rs.core.cache.loaders.ObjectDefinitions;
import com.rs.core.cores.CoresManager;
import com.rs.core.settings.SettingsManager;
import com.rs.core.utils.Logger;

public class DynamicRegion extends Region {

	// int dynamicregion squares amt
	// Region[] array with the region squares
	// int[][] squaresBounds;

	private int[][][][] regionCoords;

	private RegionMap removedMap;

	public DynamicRegion(final int regionId) {
		super(regionId);
		checkLoadMap();
		// plane,x,y,(real x, real y,or real plane coord, or rotation)
		regionCoords = new int[4][8][8][4];
	}

	@Override
	public void removeMapObject(final WorldObject object, final int x,
			final int y) {
		if (removedMap == null) {
			removedMap = new RegionMap(getRegionId(), false);
		}
		final int plane = object.getPlane();
		final int type = object.getType();
		final int rotation = object.getRotation();
		if (x < 0 || y < 0 || x >= removedMap.getMasks()[plane].length
				|| y >= removedMap.getMasks()[plane][x].length)
			return;
		final ObjectDefinitions objectDefinition = ObjectDefinitions
				.getObjectDefinitions(object.getId()); // load here

		if (type == 22 ? objectDefinition.getClipType() != 0 : objectDefinition
				.getClipType() == 0)
			return;
		if (type >= 0 && type <= 3) {
			removedMap.addWall(plane, x, y, type, rotation,
					objectDefinition.isProjectileCliped(), true);
		} else if (type >= 9 && type <= 21) {
			int sizeX;
			int sizeY;
			if (rotation != 1 && rotation != 3) {
				sizeX = objectDefinition.getSizeX();
				sizeY = objectDefinition.getSizeY();
			} else {
				sizeX = objectDefinition.getSizeY();
				sizeY = objectDefinition.getSizeX();
			}
			removedMap.addObject(plane, x, y, sizeX, sizeY,
					objectDefinition.isProjectileCliped(), true);
		} else if (type == 22) {
			// map.addFloor(plane, x, y);
		}
	}

	// override by static region to empty
	@Override
	public void checkLoadMap() {
		if (getLoadMapStage() == 0) {
			setLoadMapStage(1);
			// lets use slow executor, if we take 1-3sec to load objects who
			// cares? what maters are the players on the loaded regions lul
			CoresManager.SLOW_EXECUTOR.execute(new Runnable() {
				@Override
				public void run() {
					try {
						setLoadMapStage(2);
						if (!isLoadedObjectSpawns()) {
							loadObjectSpawns();
							setLoadedObjectSpawns(true);
						}
						if (!isLoadedNPCSpawns()) {
							loadNPCSpawns();
							setLoadedNPCSpawns(true);
						}
					} catch (final Throwable e) {
						Logger.handle(e);
					}
				}
			});
		}
	}

	@Override
	public void setMask(final int plane, final int localX, final int localY,
			final int mask) {

	}

	/*
	 * gets the real tile objects from real region
	 */
	@Override
	public WorldObject[] getObjects(final int plane, final int localX,
			final int localY) {
		final int currentChunkX = localX / 8;
		final int currentChunkY = localY / 8;
		final int rotation = regionCoords[plane][currentChunkX][currentChunkY][3];
		final int realChunkX = regionCoords[plane][currentChunkX][currentChunkY][0];
		final int realChunkY = regionCoords[plane][currentChunkX][currentChunkY][1];
		if (realChunkX == 0 || realChunkY == 0)
			return null;
		final int realRegionId = RegionBuilder.getRegionHash(realChunkX / 8,
				realChunkY / 8);
		final Region region = World.getRegion(realRegionId, true);
		if (region instanceof DynamicRegion) {
			if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
				Logger.info(this,
						"YOU CANT MAKE A REAL MAP AREA INTO A DYNAMIC REGION!, IT MAY DEADLOCK!");
			}
			return null; // no information so that data not loaded
		}
		final int realRegionOffsetX = (realChunkX - ((realChunkX / 8) * 8));
		final int realRegionOffsetY = (realChunkY - ((realChunkY / 8) * 8));
		int posInChunkX = (localX - (currentChunkX * 8));
		int posInChunkY = (localY - (currentChunkY * 8));
		if (rotation != 0) {
			for (int rotate = 0; rotate < (4 - rotation); rotate++) {
				final int fakeChunckX = posInChunkX;
				final int fakeChunckY = posInChunkY;
				posInChunkX = fakeChunckY;
				posInChunkY = 7 - fakeChunckX;
			}
		}
		final int realLocalX = (realRegionOffsetX * 8) + posInChunkX;
		final int realLocalY = (realRegionOffsetY * 8) + posInChunkY;
		return region.getObjects(
				regionCoords[plane][currentChunkX][currentChunkY][2],
				realLocalX, realLocalY);
	}

	// overrided by static region to get mask from needed region
	@Override
	public int getMaskClipedOnly(final int plane, final int localX,
			final int localY) {
		final int currentChunkX = localX / 8;
		final int currentChunkY = localY / 8;
		final int rotation = regionCoords[plane][currentChunkX][currentChunkY][3];
		final int realChunkX = regionCoords[plane][currentChunkX][currentChunkY][0];
		final int realChunkY = regionCoords[plane][currentChunkX][currentChunkY][1];
		if (realChunkX == 0 || realChunkY == 0)
			return -1;
		final int realRegionId = (((realChunkX / 8) << 8) + (realChunkY / 8));
		final Region region = World.getRegion(realRegionId, true);
		if (region instanceof DynamicRegion) {
			if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
				Logger.info(this,
						"YOU CANT MAKE A REAL MAP AREA INTO A DYNAMIC REGION!, IT MAY DEADLOCK!");
			}
			return -1; // no information so that data not loaded
		}
		final int realRegionOffsetX = (realChunkX - ((realChunkX / 8) * 8));
		final int realRegionOffsetY = (realChunkY - ((realChunkY / 8) * 8));
		int posInChunkX = (localX - (currentChunkX * 8));
		int posInChunkY = (localY - (currentChunkY * 8));
		if (rotation != 0) {
			for (int rotate = 0; rotate < (4 - rotation); rotate++) {
				final int fakeChunckX = posInChunkX;
				final int fakeChunckY = posInChunkY;
				posInChunkX = fakeChunckY;
				posInChunkY = 7 - fakeChunckX;
			}
		}
		final int realLocalX = (realRegionOffsetX * 8) + posInChunkX;
		final int realLocalY = (realRegionOffsetY * 8) + posInChunkY;
		return region.getMaskClipedOnly(
				regionCoords[plane][currentChunkX][currentChunkY][2],
				realLocalX, realLocalY);
	}

	@Override
	public int getRotation(final int plane, final int localX, final int localY) {
		return regionCoords[plane][localX / 8][localY / 8][3];
	}

	/*
	 * gets clip data from the original region part
	 */

	@Override
	public int getMask(final int plane, final int localX, final int localY) {
		final int currentChunkX = localX / 8;
		final int currentChunkY = localY / 8;
		final int rotation = regionCoords[plane][currentChunkX][currentChunkY][3];
		final int realChunkX = regionCoords[plane][currentChunkX][currentChunkY][0];
		final int realChunkY = regionCoords[plane][currentChunkX][currentChunkY][1];
		if (realChunkX == 0 || realChunkY == 0)
			return -1;
		final int realRegionId = (((realChunkX / 8) << 8) + (realChunkY / 8));
		final Region region = World.getRegion(realRegionId, true);
		if (region instanceof DynamicRegion) {
			if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
				Logger.info(this,
						"YOU CANT MAKE A REAL MAP AREA INTO A DYNAMIC REGION!, IT MAY DEADLOCK!");
			}
			return -1; // no information so that data not loaded
		}
		final int realRegionOffsetX = (realChunkX - ((realChunkX / 8) * 8));
		final int realRegionOffsetY = (realChunkY - ((realChunkY / 8) * 8));
		int posInChunkX = (localX - (currentChunkX * 8));
		int posInChunkY = (localY - (currentChunkY * 8));
		if (rotation != 0) {
			for (int rotate = 0; rotate < (4 - rotation); rotate++) {
				final int fakeChunckX = posInChunkX;
				final int fakeChunckY = posInChunkY;
				posInChunkX = fakeChunckY;
				posInChunkY = 7 - fakeChunckX;
			}
		}
		final int realLocalX = (realRegionOffsetX * 8) + posInChunkX;
		final int realLocalY = (realRegionOffsetY * 8) + posInChunkY;

		int mask = region.getMask(
				regionCoords[plane][currentChunkX][currentChunkY][2],
				realLocalX, realLocalY);

		if (removedMap != null) {
			mask = mask & (~removedMap.getMasks()[plane][localX][localY]);
		}
		return mask;
	}

	public int[][][][] getRegionCoords() {
		return regionCoords;
	}

	public void setRegionCoords(final int[][][][] regionCoords) {
		this.regionCoords = regionCoords;
	}
}
