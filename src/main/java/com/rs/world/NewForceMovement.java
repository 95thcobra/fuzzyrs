package com.rs.world;

public final class NewForceMovement extends ForceMovement {

	public NewForceMovement(final WorldTile toFirstTile,
			final int firstTileTicketDelay, final WorldTile toSecondTile,
			final int secondTileTicketDelay, final int direction) {
		super(toFirstTile, firstTileTicketDelay, toSecondTile,
				secondTileTicketDelay, direction);
	}

	@Override
	public int getDirection() {
		return direction;
	}

}
