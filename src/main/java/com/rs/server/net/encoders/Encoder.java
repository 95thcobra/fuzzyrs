package com.rs.server.net.encoders;

import com.rs.server.net.Session;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Encoder {

	protected Session session;
	protected Map<Integer, Integer> requests;

	public Encoder(Session session) {
		this.session = session;
		this.requests = Collections.synchronizedMap(new HashMap<>());
	}

	public Map<Integer, Integer> getRequests() {
		if (requests == null)
			this.requests = Collections.synchronizedMap(new HashMap<>());
		return requests;
	}
}
