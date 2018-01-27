package com.rs.world;

import com.rs.server.GameConstants;
import com.rs.utils.Utils;

import java.io.Serializable;

public class WorldTile implements Serializable {

	private static final long serialVersionUID = -6567346497259686765L;
	public boolean spawnedByEd = false;
	private short x, y;
	private byte plane;

	public WorldTile(final int x, final int y, final int plane) {
		this.x = (short) x;
		this.y = (short) y;
		this.plane = (byte) plane;
	}

	public WorldTile(final WorldTile tile) {
		this.x = tile.x;
		this.y = tile.y;
		this.plane = tile.plane;
	}

	public WorldTile(final WorldTile tile, final int randomize) {
		this.x = (short) (tile.x + Utils.getRandom(randomize * 2) - randomize);
		this.y = (short) (tile.y + Utils.getRandom(randomize * 2) - randomize);
		this.plane = tile.plane;
	}

	public WorldTile(final int hash) {
		this.x = (short) (hash >> 14 & 0x3fff);
		this.y = (short) (hash & 0x3fff);
		this.plane = (byte) (hash >> 28);
	}

	public static int getCoordFaceX(final int x, final int sizeX,
									final int sizeY, final int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	public static int getCoordFaceY(final int y, final int sizeX,
									final int sizeY, final int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}

	public void moveLocation(final int xOffset, final int yOffset,
			final int planeOffset) {
		x += xOffset;
		y += yOffset;
		plane += planeOffset;
	}

	public final void setLocation(final WorldTile tile) {
		setLocation(tile.x, tile.y, tile.plane);
	}

	public final void setLocation(final int x, final int y, final int plane) {
		this.x = (short) x;
		this.y = (short) y;
		this.plane = (byte) plane;
	}

	public int getX() {
		return x;
	}

	public int getXInRegion() {
		return x & 0x3F;
	}

	public int getYInRegion() {
		return y & 0x3F;
	}

	public int getY() {
		return y;
	}

	public int getPlane() {
		if (plane > 3)
			return 3;
		return plane;
	}

	public int getChunkX() {
		return (x >> 3);
	}

	public int getChunkY() {
		return (y >> 3);
	}

	public int getRegionX() {
		return (x >> 6);
	}

	public int getRegionY() {
		return (y >> 6);
	}

	public int getRegionId() {
		return ((getRegionX() << 8) + getRegionY());
	}

	public int getLocalX(final WorldTile tile, final int mapSize) {
		return x - 8 * (tile.getChunkX() - (GameConstants.MAP_SIZES[mapSize] >> 4));
	}

	public int getLocalY(final WorldTile tile, final int mapSize) {
		return y - 8 * (tile.getChunkY() - (GameConstants.MAP_SIZES[mapSize] >> 4));
	}

	public int getLocalX(final WorldTile tile) {
		return getLocalX(tile, 0);
	}

	public int getLocalY(final WorldTile tile) {
		return getLocalY(tile, 0);
	}

	public int getLocalX() {
		return getLocalX(this);
	}

	public int getLocalY() {
		return getLocalY(this);
	}

	public int getRegionHash() {
		return getRegionY() + (getRegionX() << 8) + (plane << 16);
	}

	public int getTileHash() {
		return y + (x << 14) + (plane << 28);
	}

	public boolean withinDistance(final WorldTile tile, final int distance) {
		if (tile.plane != plane)
			return false;
		final int deltaX = tile.x - x, deltaY = tile.y - y;
		return deltaX <= distance && deltaX >= -distance && deltaY <= distance
				&& deltaY >= -distance;
	}

	public boolean withinDistance(final WorldTile tile) {
		if (tile.plane != plane)
			return false;
		// int deltaX = tile.x - x, deltaY = tile.y - y;
		return Math.abs(tile.x - x) <= 14 && Math.abs(tile.y - y) <= 14;// deltaX
		// <= 14
		// &&
		// deltaX
		// >=
		// -15
		// &&
		// deltaY
		// <= 14
		// &&
		// deltaY
		// >=
		// -15;
	}

	public int getCoordFaceX(final int sizeX) {
		return getCoordFaceX(sizeX, -1, -1);
	}

	public int getCoordFaceX(final int sizeX, final int sizeY,
			final int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	public int getCoordFaceY(final int sizeY) {
		return getCoordFaceY(-1, sizeY, -1);
	}

	public int getCoordFaceY(final int sizeX, final int sizeY,
			final int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}

	public WorldTile transform(final int x, final int y, final int plane) {
		return new WorldTile(this.x + x, this.y + y, this.plane + plane);
	}

	/**
	 * Checks if this worldtask tile's coordinates match the other worldtask tile.
	 * 
	 * @param other
	 *            The worldtask tile to compare with.
	 * @return {@code True} if so.
	 */
	public boolean matches(final WorldTile other) {
		return x == other.x && y == other.y && plane == other.plane;
	}

    public enum Direction {

        NORTH(0), SOUTH(1), EAST(-1), WEST(2);

        private final int direction;

        Direction(int direction) {
            this.direction = direction;
        }

        public int getDirection() {
            return direction;
        }
    }
}
