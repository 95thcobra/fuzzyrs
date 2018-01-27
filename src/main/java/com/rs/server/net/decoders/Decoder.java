package com.rs.server.net.decoders;

import com.rs.server.net.Session;
import com.rs.server.net.io.InputStream;

public abstract class Decoder {

	protected Session session;

	public Decoder(final Session session) {
		this.session = session;
	}

	public abstract void decode(InputStream stream);

}
