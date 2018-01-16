package com.rs.world;

public final class Graphics {

	private final int id, height, speed, rotation;

	public Graphics(final int id) {
		this(id, 0, 0, 0);

	}

	public Graphics(final int id, final int speed, final int height) {
		this(id, speed, height, 0);
	}

	public Graphics(final int id, final int speed, final int height,
			final int rotation) {
		this.id = id;
		this.speed = speed;
		this.height = height;
		this.rotation = rotation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + id;
		result = prime * result + rotation;
		result = prime * result + speed;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Graphics other = (Graphics) obj;
		return height == other.height && id == other.id && rotation == other.rotation && speed == other.speed;
	}

	public int getId() {
		return id;
	}

	public int getSettingsHash() {
		return (speed & 0xffff) | (height << 16);
	}

	public int getSettings2Hash() {
		int hash = 0;
		hash |= rotation & 0x7;
		// hash |= value << 3;
		// hash |= 1 << 7; boolean
		return hash;
	}

	public int getSpeed() {
		return speed;
	}

	public int getHeight() {
		return height;
	}
}
