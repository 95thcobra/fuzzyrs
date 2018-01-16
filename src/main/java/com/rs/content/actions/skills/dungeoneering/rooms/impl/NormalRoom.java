package com.rs.content.actions.skills.dungeoneering.rooms.impl;

import com.rs.content.actions.skills.dungeoneering.dungeon.DungeonManager;

public final class NormalRoom extends HandledRoom {

	private int complexity;

	public NormalRoom(int complexity, int chunkX, int chunkY,
			int... doorsDirections) {
		super(chunkX, chunkY, DungeonManager::spawnRandomNPCS, doorsDirections);
		this.complexity = complexity;
	}

	@Override
	public boolean isComplexity(int complexity) {
		return this.complexity <= complexity;
	}

}
