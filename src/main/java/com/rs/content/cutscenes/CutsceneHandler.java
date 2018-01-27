package com.rs.content.cutscenes;

import com.rs.content.cutscenes.impl.*;
import com.rs.utils.Logger;

import java.util.HashMap;

public class CutsceneHandler {

	private static final HashMap<Object, Class<Cutscene>> handledCutscenes = new HashMap<Object, Class<Cutscene>>();

	@SuppressWarnings("unchecked")
	public static void init() {
		try {
			final Class<Cutscene> value1 = (Class<Cutscene>) Class
					.forName(EdgeWilderness.class.getCanonicalName());
			handledCutscenes.put("EdgeWilderness", value1);
			final Class<Cutscene> value2 = (Class<Cutscene>) Class
					.forName(DTPreview.class.getCanonicalName());
			handledCutscenes.put("DTPreview", value2);
			final Class<Cutscene> value3 = (Class<Cutscene>) Class
					.forName(NexCutScene.class.getCanonicalName());
			handledCutscenes.put("NexCutScene", value3);
			final Class<Cutscene> value4 = (Class<Cutscene>) Class
					.forName(TowersPkCutscene.class.getCanonicalName());
			handledCutscenes.put("TowersPkCutscene", value4);
			final Class<Cutscene> value5 = (Class<Cutscene>) Class
					.forName(HomeCutScene.class.getCanonicalName());
			handledCutscenes.put("HomeCutScene", value5);
			final Class<Cutscene> value6 = (Class<Cutscene>) Class
					.forName(MasterOfFear.class.getCanonicalName());
			handledCutscenes.put("MasterOfFear", value6);
		} catch (final Throwable e) {
			Logger.handle(e);
		}
	}

	public static void reload() {
		handledCutscenes.clear();
		init();
	}

	public static Cutscene getCutscene(final Object key) {
		final Class<Cutscene> classC = handledCutscenes.get(key);
		if (classC == null)
			return null;
		try {
			return classC.newInstance();
		} catch (final Throwable e) {
			Logger.handle(e);
		}
		return null;
	}
}
