package com.rs.game.player.content;

import java.io.Serializable;

import com.rs.game.player.Player;

public class LoyaltyProgramme implements Serializable {

	/**
	 * The serial UID
	 */
	private static final long serialVersionUID = -111881367666488484L;

	/**
	 * The loyalty shop interface
	 */
	public static final int INTERFACE_ID = 1143;

	/**
	 * The tab switch config
	 */
	public static final int TAB_CONFIG = 2226;

	/**
	 * The current tab
	 */
	private int currentTab;

	/**
	 * The player using the programme
	 */
	private Player player;

	/**
	 * Opens the loyalty shop interface
	 */
	public void openShop() {
		player.getInterfaceManager().sendScreenInterface(96, INTERFACE_ID);
		player.getPackets().sendConfig(TAB_CONFIG, -1);
		currentTab = -1;
		player.getPackets().sendIComponentText(INTERFACE_ID, 127,
				"" + player.getLoyaltyPoints());
	}

	/**
	 * Opens a tab on the loyalty interface
	 * 
	 * @param tab
	 *            The tab to open
	 */
	public void openTab(String tab) {
		switch (tab.toLowerCase()) {
		case "home":
			player.getPackets().sendConfig(TAB_CONFIG, -1);
			currentTab = -1;
		case "auras":
			player.getPackets().sendConfig(TAB_CONFIG, 1);
			currentTab = 1;
			break;
		case "emotes":
			player.getPackets().sendConfig(TAB_CONFIG, 2);
			currentTab = 2;
			break;
		case "outfits":
			player.getPackets().sendConfig(TAB_CONFIG, 3);
			currentTab = 3;
			break;
		case "titles":
			player.getPackets().sendConfig(TAB_CONFIG, 4);
			currentTab = 4;
			break;
		case "recolor":
			player.getPackets().sendConfig(TAB_CONFIG, 5);
			currentTab = 5;
			break;
		case "special-offers":
			player.getPackets().sendConfig(TAB_CONFIG, 6);
			currentTab = 6;
			break;
		case "limmited-edition":
			player.getPackets().sendConfig(TAB_CONFIG, 7);
			currentTab = 7;
			break;
		case "favorites":
			player.getPackets().sendConfig(TAB_CONFIG, 8);
			currentTab = 8;
			break;
		case "effects":
			player.getPackets().sendConfig(TAB_CONFIG, 9);
			currentTab = 9;
			break;
		default:
			player.getPackets().sendGameMessage(
					"This tab is currently un-available"
							+ (player.getRights() >= 2 ? ": " + "\"" + tab
									+ "\"" : "."));
		}
	}

	/**
	 * Handles any button clicks
	 * 
	 * @param componentId
	 *            The clicked component
	 * @param slotId
	 *            The clicked slot
	 * @param slotId2
	 *            The clicked slot (2)
	 * @param packetId
	 *            The packet ID
	 */
	public void handleButtons(int componentId, int slotId, int slotId2,
			int packetId) {
		switch (componentId) {
		case 3:
			openTab("favorites");
			break;
		case 103:
			player.getPackets().sendWindowsPane(
					player.getInterfaceManager().hasRezizableScreen() ? 746
							: 548, 0);
			break;
		case 1:
			openTab("home");
			break;
		case 7:
			openTab("auras");
			break;
		case 8:
			openTab("effects");
			break;
		case 9:
			openTab("emotes");
			break;
		case 10:
			openTab("outfits");
			break;
		case 11:
			openTab("titles");
			break;
		case 12:
			openTab("recolor");
			break;
		case 13:
			openTab("special-offers");
			break;
		}
	}

	/**
	 * Favorites an item
	 * 
	 * @param value
	 *            The item to favorite
	 */
	public void favorite(int value) {
		player.getPackets().sendConfig(2391, value);
	}

	/**
	 * Claims an item
	 * 
	 * @param value
	 *            The item to claim
	 */
	public void claim(int value) {
		player.getPackets().sendConfig(2229, value);
		player.getPackets().sendConfig(TAB_CONFIG, currentTab);// refresh tab
	}

	/**
	 * Sets the player
	 * 
	 * @param player
	 *            The player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
}