package com.rs.player.controlers;

import com.rs.content.actions.skills.construction.House;
import com.rs.world.region.RegionBuilder;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;

public class HouseController extends Controller {

	boolean remove = true;
	private House house;
	private int[] boundChuncks;

	@Override
	public void start() {
		house = new House();
		boundChuncks = RegionBuilder.findEmptyChunkBound(8, 8);
		house.constructHouse(boundChuncks, false);
		player.setNextWorldTile(new WorldTile(boundChuncks[0] * 8 + 35,
				boundChuncks[1] * 8 + 35, 0));
	}

	/**
	 * return process normaly
	 */
	@Override
	public boolean processObjectClick5(final WorldObject object) {
		house.previewRoom(player, boundChuncks, new House.RoomReference(House.Room.PARLOUR,
				4, 5, 0, 0), remove = !remove);
		return true;
	}

}
