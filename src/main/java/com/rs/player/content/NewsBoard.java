package com.rs.player.content;

import com.rs.core.settings.SettingsManager;
import com.rs.player.Player;

/**
 * Displays the news board when the player logs in.
 *
 * @author FuzzyAvacado
 */
public class NewsBoard {

	private static final int INTERFACE_ID = 1151;
	private static final String TITLE = "FuzzyRs News Board";
	private static final String[] NEWS = {
			"Type <col=ff0000>::updates</col> for all updates.",
			"<col=ff0000>Multiple events</col> will be held on sundays.",
			"Donate by typing <col=ff0000>::donate</col>!",
			"Don't forget to check the quest tab.",
			"Don't forget to vote at <col=ff0000>::vote</col>." };

	public static void display(final Player player, final int completed) {
		if (!SettingsManager.getSettings().NEWS_BOARD)
			return;
		if (completed == 1) {
			player.getInterfaceManager().sendInterface(1151);
			player.getPackets().sendIComponentText(INTERFACE_ID, 33, TITLE);
			player.getPackets().sendIComponentText(INTERFACE_ID, 35, NEWS[0]);
			player.getPackets().sendIComponentText(INTERFACE_ID, 36, "");
			player.getPackets().sendIComponentText(INTERFACE_ID, 47, NEWS[1]);
			player.getPackets().sendIComponentText(INTERFACE_ID, 48, "");
			player.getPackets().sendIComponentText(INTERFACE_ID, 59, NEWS[2]);
			player.getPackets().sendIComponentText(INTERFACE_ID, 60, "");
			player.getPackets().sendIComponentText(INTERFACE_ID, 38, NEWS[3]);
			player.getPackets().sendIComponentText(INTERFACE_ID, 39, "");
			player.getPackets().sendIComponentText(INTERFACE_ID, 44, NEWS[4]);
			player.getPackets().sendIComponentText(INTERFACE_ID, 45, "");
			player.getPackets().sendIComponentText(INTERFACE_ID, 56, " ");
			player.getPackets().sendIComponentText(INTERFACE_ID, 57, " ");
		} else {
			player.getPackets()
			.sendGameMessage(
					"Since this is your first time playing, the news interface was disabled. You can enable it by logging out and back in.");
		}
	}

}
