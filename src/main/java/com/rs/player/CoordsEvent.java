package com.rs.player;

import com.rs.world.WorldTile;

public final class CoordsEvent {

	private final WorldTile tile;
	private final Runnable event;
	private int sizeX;
	private int sizeY;

	public CoordsEvent(final WorldTile tile, final Runnable event,
			final int sizeX, final int sizeY, final int rotation) {
		this.tile = tile;
		this.event = event;
		if (rotation == 1 || rotation == 3) {
			this.sizeX = sizeY;
			this.sizeY = sizeX;
		} else {
			this.sizeX = sizeX;
			this.sizeY = sizeY;
		}
	}

	public CoordsEvent(final WorldTile tile, final Runnable event,
			final int sizeX, final int sizeY) {
		this(tile, event, sizeX, sizeY, -1);
	}

	public CoordsEvent(final WorldTile tile, final Runnable event,
			final int size) {
		this(tile, event, size, size);
	}

	/*
	 * returns if done
	 */
	public boolean processEvent(final Player player) {
		if (player.getPlane() != tile.getPlane())
			return true;
		final int distanceX = player.getX() - tile.getX();
		final int distanceY = player.getY() - tile.getY();
		if (distanceX > sizeX || distanceX < -1 || distanceY > sizeY
				|| distanceY < -1)
			return cantReach(player);
		if (player.hasWalkSteps()) {
			player.resetWalkSteps();
		}
		event.run();
		return true;
	}

	public boolean cantReach(final Player player) {
		if (!player.hasWalkSteps() && player.getNextWalkDirection() == -1) {
			player.getPackets().sendGameMessage("You can't reach that.");
			return true;
		}
		return false;
	}
}