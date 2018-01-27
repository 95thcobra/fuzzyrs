package com.rs.player.content;

import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.server.Server;

/**
 * @author JazzyYaYaYa | Fuzen Seth | Nexon
 */
public class DisplayNameAction {

    public static int MoneyAmount = 50000000;

	public static void RemoveDisplay(final Player player) {
		player.setDisplayName(Utils.formatPlayerNameForDisplay(player
				.getUsername()));
		player.getInterfaceManager().closeChatBoxInterface();
		Server.getInstance().getPlayerFileManager().save(player);
		player.getPackets()
				.sendGameMessage(
						"Your display name has been removed. You must re-login for it to take effect.");
	}

	public static void ProcessChange(final Player player) {
		if (player.getInventory().containsItem(995, MoneyAmount)) {
			// player.getInventory().deleteItem(995, MoneyAmount);
			player.getInventory().refresh();
			player.getInterfaceManager().closeChatBoxInterface();
			player.getTemporaryAttributtes().put("setdisplay", Boolean.TRUE);
			player.getPackets().sendInputNameScript(
					"Enter the display name you wish:");

		} else {
			player.getInterfaceManager().closeChatBoxInterface();
			player.getPackets().sendGameMessage(
					"Changing your display name will cost 50 million coins.");
		}
	}

}
