package com.rs.player;

import com.rs.world.WorldObject;

public class PossessedObject extends WorldObject {

	/**
	 *
	 */
	private static final long serialVersionUID = -543150569322118775L;

	private Player owner;

	public PossessedObject(final Player owner, final int id, final int type,
			final int rotation, final int x, final int y, final int plane) {
		super(id, type, rotation, x, y, plane);
		setOwner(owner);
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(final Player owner) {
		this.owner = owner;
	}
}
