package com.rs.content.actions.skills.dungeoneering.rooms;

import com.rs.content.actions.skills.dungeoneering.dungeon.DungeonManager;

public interface RoomEvent {

	void openRoom(DungeonManager dungeon, RoomReference reference);
}
