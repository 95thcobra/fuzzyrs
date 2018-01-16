package com.rs.content.minigames.castlewars;

import com.rs.world.WorldTile;

public class CastleWarsConstants {

	public static final int CW_TICKET = 4067;
	public static final int SARADOMIN = 0;
	public static final int ZAMORAK = 1;
	public static final int GUTHIX = 2;

	public static final WorldTile LOBBY = new WorldTile(2442, 3090, 0),
			SARA_WAITING = new WorldTile(2381, 9489, 0),
			ZAMO_WAITING = new WorldTile(2421, 9523, 0),
			SARA_BASE = new WorldTile(2426, 3076, 1),
			ZAMO_BASE = new WorldTile(2373, 3131, 1);
	public static final int PLAYERS_TO_START = 5;
}
