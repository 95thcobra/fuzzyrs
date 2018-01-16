package com.rs.core.net.decoders;

import com.rs.core.net.Session;
import com.rs.core.net.io.InputStream;

public abstract class Decoder {

	protected Session session;

	public Decoder(final Session session) {
		this.session = session;
	}

	public abstract void decode(InputStream stream);

}
