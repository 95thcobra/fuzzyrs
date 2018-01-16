package com.rs.game.player.quests.impl;

import java.io.Serializable;

import com.rs.game.Entity;
import com.rs.game.player.Player;
import com.rs.utils.SerializableFilesManager;


public class PriestInPeril {
	

	
	public static boolean startedPriestinPeril;

	public static void handleProgressQuest(final Player player) {
		Player.startedPriestinPeril = true;
		Player.inProgressPriestinPeril = true;
		player.getPackets().sendConfig(29, 1);
		player.getPackets().sendConfig(35, 3);
		player.getPackets().sendConfig(75, 3);
		player.getPackets().sendConfig(56, 3);
		player.getPackets().sendConfig(80, 3);
		
		player.getInterfaceManager().sendInterfaces();
		player.getPackets().sendUnlockIComponentOptionSlots(190, 15, 0, 201, 0, 1, 2, 3);
	}








}
