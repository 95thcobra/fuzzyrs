package com.rs.game.randomevents;

import java.util.HashMap;

import com.rs.game.randomevents.impl.DrillDemon;
import com.rs.game.randomevents.impl.EvilBob;
import com.rs.game.randomevents.impl.RunePinball;
import com.rs.game.randomevents.impl.SandwichLady;
import com.rs.utils.Logger;

public class RandomEventHandler {
	
	private static final HashMap<Object, Class<RandomEvent>> events = new HashMap<Object, Class<RandomEvent>>();

	@SuppressWarnings("unchecked")
	public static final void init() {
		try {
			events.put("RunePinball", (Class<RandomEvent>) Class.forName(RunePinball.class.getCanonicalName()));
			events.put("SandwichLady", (Class<RandomEvent>) Class.forName(SandwichLady.class.getCanonicalName()));
			/*
			 * Disabled because the server can't run...
			 */
			events.put("DrillDemon", (Class<RandomEvent>) Class.forName(DrillDemon.class.getCanonicalName()));
			events.put("EvilBob", (Class<RandomEvent>) Class.forName(EvilBob.class.getCanonicalName()));
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}
	
	public static final void reload() {
		events.clear();
		init();
	}

	public static final RandomEvent getRandomEvent(Object key) {
		if (key instanceof RandomEvent)
			return (RandomEvent) key;
		Class<RandomEvent> classC = events.get(key);
		if (classC == null)
			return null;
		try {
			return classC.newInstance();
		} catch (Throwable e) {
			Logger.handle(e);
		}
		return null;
	}

}
