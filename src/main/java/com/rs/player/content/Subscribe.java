package com.rs.player.content;

import com.rs.content.dialogues.impl.ClassPick;
import com.rs.content.player.PlayerRank;
import com.rs.core.settings.SettingsManager;
import com.rs.player.Player;

/**
 * Created with IntelliJ IDEA. User: JazzyYaYaYa Date: 20.1.2013 Time: 17:05
 */
public class Subscribe {
	public static int BOARD = 1067;

	/* This sends the actual board upon login. */
	public static void SubBoard(final Player player) {
		if (player.getRank().getDonateRank().isMinimumRank(PlayerRank.DonateRank.DONATOR)) { /* Membership screen */
			player.getPackets().sendIComponentText(1067, 17,
					"Welcome back to " + SettingsManager.getSettings().SERVER_NAME + ", thanks for donating.");
			player.getPackets()
					.sendIComponentText(1067, 23, "Continue Playing");
			player.getPackets()
					.sendIComponentText(1067, 31, "Visit the Forums");
			player.getInterfaceManager().sendInterface(BOARD);
		} else { // F2p Screen
			player.getPackets().sendIComponentText(1067, 17,
					"Welcome back to " + SettingsManager.getSettings().SERVER_NAME + ".");
			player.getPackets()
					.sendIComponentText(1067, 23, "Continue Playing");
			player.getPackets()
					.sendIComponentText(1067, 31, "Visit the Forums");
			player.getInterfaceManager().sendInterface(BOARD);
		}
	}

	public static void detectPlayerStatus(final Player player) {
		if (player.starter == 0) {
			player.getDialogueManager().startDialogue(ClassPick.class);
			player.lock();
			player.chooseChar = 1;
			player.starter = 1;
		}
	}

	public static void handleButtons(final Player player, final int componentId) {
		if (componentId == 0) { // Play now button
			player.getInventory().refresh();
			player.closeInterfaces();
			player.getPackets().sendGameMessage(
					"It's glad to see you're back, have fun.");
			detectPlayerStatus(player);
		}
		if (componentId == 24) { // Visit website
			player.getPackets().sendOpenURL(
					SettingsManager.getSettings().FORUM_LINK);
			player.getInventory().refresh();
			player.closeInterfaces();
			detectPlayerStatus(player);
		}
		if (componentId == 12) {// Gtfo button
			player.getInventory().refresh();
			player.closeInterfaces();
			detectPlayerStatus(player);
		}
	}
}