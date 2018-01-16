package com.rs.world;

public final class Animation {

	private final int[] ids;
	private final int speed;

	public Animation(final int id) {
		this(id, 0);
	}

	public Animation(final int id, final int speed) {
		this(id, id, id, id, speed);
	}

	public Animation(final int id1, final int id2, final int id3,
			final int id4, final int speed) {
		this.ids = new int[] { id1, id2, id3, id4 };
		this.speed = speed;
	}

	public int[] getIds() {
		return ids;
	}

	public int getSpeed() {
		return speed;
	}
}
