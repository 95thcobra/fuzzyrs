package com.rs.player;

import com.rs.server.net.io.InputStream;

public class LogicPacket {

	private final int id;
	byte[] data;

	public LogicPacket(final int id, final int size, final InputStream stream) {
		this.id = id;
		data = new byte[size];
		stream.getBytes(data, 0, size);
	}

	public int getId() {
		return id;
	}

	public byte[] getData() {
		return data;
	}

}
