package com.rs.world;

import com.rs.core.utils.Utils;

public class ForceMovement {

	public static final int NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3;

	private final WorldTile toFirstTile;
	private final WorldTile toSecondTile;
	private final int firstTileTicketDelay;
	private final int secondTileTicketDelay;
	protected int direction;

	/*
	 * USE: moves to firsttile firstTileTicketDelay: the delay in gametask tickets
	 * between your tile and first tile the direction
	 */
	public ForceMovement(final WorldTile toFirstTile,
			final int firstTileTicketDelay, final int direction) {
		this(toFirstTile, firstTileTicketDelay, null, 0, direction);
	}

	/*
	 * USE: moves to firsttile and from first tile to second tile
	 * firstTileTicketDelay: the delay in gametask tickets between your tile and
	 * first tile secondTileTicketDelay: the delay in gametask tickets between first
	 * tile and second tile the direction
	 */
	public ForceMovement(final WorldTile toFirstTile,
			final int firstTileTicketDelay, final WorldTile toSecondTile,
			final int secondTileTicketDelay, final int direction) {
		this.toFirstTile = toFirstTile;
		this.firstTileTicketDelay = firstTileTicketDelay;
		this.toSecondTile = toSecondTile;
		this.secondTileTicketDelay = secondTileTicketDelay;
		this.direction = direction;
	}

	public int getDirection() {
		switch (direction) {
		case NORTH:
			return Utils.getFaceDirection(0, 1);
		case EAST:
			return Utils.getFaceDirection(1, 0);
		case SOUTH:
			return Utils.getFaceDirection(0, -1);
		case WEST:
		default:
			return Utils.getFaceDirection(-1, 0);
		}
	}

	public WorldTile getToFirstTile() {
		return toFirstTile;
	}

	public WorldTile getToSecondTile() {
		return toSecondTile;
	}

	public int getFirstTileTicketDelay() {
		return firstTileTicketDelay;
	}

	public int getSecondTileTicketDelay() {
		return secondTileTicketDelay;
	}

}
