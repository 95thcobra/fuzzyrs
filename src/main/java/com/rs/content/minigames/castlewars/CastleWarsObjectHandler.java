package com.rs.content.minigames.castlewars;

import com.rs.content.dialogues.impl.CastleWarsScoreboard;
import com.rs.player.Player;

public class CastleWarsObjectHandler {

	public static void handleInterfaces(final Player player,
			final int interfaceId, final int componentId) {
		if (interfaceId == 55) {
			if (componentId == 9) {
				player.closeInterfaces();
			}
		}
	}

	public static boolean handleObjects(Player player, int objectId) {
		System.out.println("Objectid: " + objectId);
		if (objectId == 4484) {
			player.getDialogueManager().startDialogue(
					new CastleWarsScoreboard());
			return true;
		}
		if (objectId == 4388) {
			CastleWars.joinPortal(player, CastleWarsConstants.ZAMORAK);
			return true;
		}
		if (objectId == 4408) {
			CastleWars.joinPortal(player, CastleWarsConstants.GUTHIX);
			return true;
		}
		if (objectId == 4387) {
			CastleWars.joinPortal(player, CastleWarsConstants.SARADOMIN);
			return true;
		}
		return false;
	}
	/** zamorak base **/
	//36585 - Pickaxe table
	//36582 - barricades table
	//36580 - toolkits table
	//36584 - explosive potion table
	//36581 - rocks table
	//2207 - flares table
	//36583 - ropes table
	//36694 - ladder to cave
	
	/** saradomin base **/
}
