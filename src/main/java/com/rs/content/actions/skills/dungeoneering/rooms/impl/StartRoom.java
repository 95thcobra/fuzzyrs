package com.rs.content.actions.skills.dungeoneering.rooms.impl;

import com.rs.content.actions.skills.dungeoneering.dungeon.DungeonManager;
import com.rs.content.actions.skills.dungeoneering.rooms.RoomEvent;
import com.rs.content.actions.skills.dungeoneering.rooms.RoomReference;
import com.rs.core.utils.Utils;

public final class StartRoom extends HandledRoom {

	private int[] complexitys;

	public StartRoom(int[] complexitys, int chunkX, int chunkY, int musicId,
			int... doorsDirections) {
		super(chunkX, chunkY, new RoomEvent() {
			@Override
			public void openRoom(DungeonManager dungeon, RoomReference reference) {
				dungeon.telePartyToRoom(reference);
				dungeon.spawnNPC(reference, 11226, 6 + Utils.random(3),
						6 + Utils.random(3)); // smoother
				dungeon.linkPartyToDungeon();
			}
		}, doorsDirections);
		this.complexitys = complexitys;

	}

	@Override
	public boolean isComplexity(int complexity) {
		for (int c : complexitys)
			if (c == complexity)
				return true;
		return false;
	}

}
