package com.rs.game.player.controlers;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.player.controlers.Controler;

/**
 * @author Gircat <gircat101@gmail.com> 
 * Created on Jul 20, 2014 at 11:01:57 PM.
 */

public final class Beginer extends Controler {

	public static final int MINING_INSTRUCTOR = 948;
	public static final int TELETAB = 8009;
	
	@Override
	public void start() {
		sendInterfaces();
	}
	
	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().closeCombatStyles();
		player.getInterfaceManager().closeTaskSystem();
//		player.getInterfaceManager().closeSkills();
		player.getInterfaceManager().closeInventory();
//		player.getInterfaceManager().closeEquipment();
		player.getInterfaceManager().closePrayerBook();
		player.getInterfaceManager().closeMagicBook();
		player.getInterfaceManager().closeEmotes();
		player.getInterfaceManager().closeFriends();
		player.getInterfaceManager().closeQuestTab();
		player.getInterfaceManager().closeFriendsChat();
		player.getInterfaceManager().closeClanChat();
//		player.getInterfaceManager().closeSettings();
		player.getInterfaceManager().closeMusic();
		player.getInterfaceManager().closeNotes();
		player.getInterfaceManager().replaceRealChatBoxInterface(372);
		for (int i = 0; i < 6; i++)
			player.getPackets().sendIComponentText(372, i, "");
		player.getPackets().sendIComponentText(372, 0,
				"Welcome to " + Settings.SERVER_NAME + ", " + player.getDisplayName() +".");
		NPC mining = findNPC(MINING_INSTRUCTOR);
				if (mining != null)
					player.getHintIconsManager().addHintIcon(mining, 0, -1, false);
		player.getPackets().sendIComponentText(372, 3, "Speak to the Mining instructor.");

	}

	public NPC findNPC(int id) {
		for (NPC npc : World.getNPCs()) {
			if (npc == null || npc.getId() != id)
				continue;
			return npc;
		}
		return null;
	}
	
}
