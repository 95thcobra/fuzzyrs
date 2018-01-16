package com.rs.game.player;

import java.io.Serializable;

import com.rs.game.WorldObject;


public class DoomSayerButtons implements Serializable {

	public boolean WildernessWall;
	
	
	
	public boolean getWildernessWall() {
		return WildernessWall;
	}
	
	public void setWildernessWall(boolean WildernessWall) {
		this.WildernessWall = WildernessWall;
	}
}
