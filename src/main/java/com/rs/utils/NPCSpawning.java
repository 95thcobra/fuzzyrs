package com.rs.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.utils.Utils.EntityDirection;

public class NPCSpawning {

	/**
	 * Contains the custom npc spawning
	 */

	public static void spawnNPCS() {

		World.spawnNPC(13930, new WorldTile(2967, 3378, 0), -1, true, true); //Ariane
		World.spawnNPC(945, new WorldTile(2968, 3366, 0), 0, false, false); //RUNESCAPE_GUIDE
		World.spawnNPC(548, new WorldTile(2944, 3381, 0), 0, false, false); //Thessalia
		World.spawnNPC(13727, new WorldTile(2964, 3388, 0), 0, false, EntityDirection.SOUTHEAST); //Xuan
		
		
		/**
		 * Contains object spawns.
		 */
		//World.spawnObject(new WorldObject(56933, 10, 0, 2964, 3380, 0), true); //xmas tree
		World.spawnObject(new WorldObject(1, 10, 0, 3081, 9502, 0), true); //crate
		World.spawnObject(new WorldObject(2507, 10, 0, 2911, 5201, 0), true); //nex portal
		World.spawnObject(new WorldObject(11448, 10, 3, 2961, 3386, 0), true); //rewards tent
		World.spawnObject(new WorldObject(884, 10, 3, 2959, 3377, 0), true); //xp well

	}
	


	/**
	 * The NPC classes.
	 */
	private static final Map<Integer, Class<?>> CUSTOM_NPCS = new HashMap<Integer, Class<?>>();

	public static void npcSpawn() {
		int size = 0;
		boolean ignore = false;
		try {
			for (String string : FileUtilities
					.readFile("data/npcs/npcspawns.txt")) {
				if (string.startsWith("//") || string.equals("")) {
					continue;
				}
				if (string.contains("/*")) {
					ignore = true;
					continue;
				}
				if (ignore) {
					if (string.contains("*/")) {
						ignore = false;
					}
					continue;
				}
				String[] spawn = string.split(" ");
				@SuppressWarnings("unused")
				int id = Integer.parseInt(spawn[0]), x = Integer
						.parseInt(spawn[1]), y = Integer.parseInt(spawn[2]), z = Integer
						.parseInt(spawn[3]), faceDir = Integer
						.parseInt(spawn[4]);
				NPC npc = null;
				Class<?> npcHandler = CUSTOM_NPCS.get(id);
				if (npcHandler == null) {
					npc = new NPC(id, new WorldTile(x, y, z), -1, true, false);
				} else {
					npc = (NPC) npcHandler.getConstructor(int.class)
							.newInstance(id);
				}
				if (npc != null) {
					WorldTile spawnLoc = new WorldTile(x, y, z);
					npc.setLocation(spawnLoc);
					World.spawnNPC(npc.getId(), spawnLoc, -1, true, false);
					size++;
				}
			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		}
		System.err.println("Loaded " + size + " custom npc spawns!");
	}

}