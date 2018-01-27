package com.rs.player;

import com.rs.server.Server;
import com.rs.content.player.points.PlayerPoints;
import com.rs.task.gametask.impl.QuestTabUpdateTask;

import java.util.concurrent.ConcurrentHashMap;

public class InterfaceManager {

	public static final int FIXED_WINDOW_ID = 548;
	public static final int RESIZABLE_WINDOW_ID = 746;
	public static final int CHAT_BOX_TAB = 13;
	public static final int FIXED_SCREEN_TAB_ID = 27;
	public static final int RESIZABLE_SCREEN_TAB_ID = 28;
	public static final int FIXED_INV_TAB_ID = 166;
	public static final int RESIZABLE_INV_TAB_ID = 108;
	private final ConcurrentHashMap<Integer, int[]> OPENED_INTERFACES = new ConcurrentHashMap<>();
	public Player player;
	private boolean resizableScreen;
	private int windowsPane;

	public InterfaceManager(final Player player) {
		this.player = player;
	}

	public void sendTab(final int tabId, final int interfaceId) {
		player.getPackets().sendInterface(true,
				resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID, tabId,
				interfaceId);
	}

	public void sendChatBoxInterface(final int interfaceId) {
		player.getPackets().sendInterface(true, 752, CHAT_BOX_TAB, interfaceId);
	}

	public void closeChatBoxInterface() {
		player.getPackets().closeInterface(CHAT_BOX_TAB);
	}

	public void sendOverlay(final int interfaceId, final boolean fullScreen) {
		sendTab(resizableScreen ? fullScreen ? 1 : 11 : 0, interfaceId);
	}

	public void closeOverlay(final boolean fullScreen) {
		player.getPackets().closeInterface(
				resizableScreen ? fullScreen ? 1 : 11 : 0);
	}

	public void sendInterface(final int interfaceId) {
		player.getPackets()
				.sendInterface(
						false,
						resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID,
						resizableScreen ? RESIZABLE_SCREEN_TAB_ID
								: FIXED_SCREEN_TAB_ID, interfaceId);
	}

	public void sendInventoryInterface(final int childId) {
		player.getPackets().sendInterface(false,
				resizableScreen ? RESIZABLE_WINDOW_ID : FIXED_WINDOW_ID,
				resizableScreen ? RESIZABLE_INV_TAB_ID : FIXED_INV_TAB_ID,
				childId);
	}

	public final void sendInterfaces() {
		if (player.getDisplayMode() == 2 || player.getDisplayMode() == 3) {
			resizableScreen = true;
			sendFullScreenInterfaces();
		} else {
			resizableScreen = false;
			sendFixedInterfaces();
		}
		player.getSkills().sendInterfaces();
		player.getCombatDefinitions().sendUnlockAttackStylesButtons();
		player.getMusicsManager().unlockMusicPlayer();
		player.getEmotesManager().unlockEmotesBook();
		player.getInventory().unlockInventoryOptions();
		player.getPrayer().unlockPrayerBookButtons();
		if (player.getFamiliar() != null && player.isRunning()) {
			player.getFamiliar().unlock();
		}
		player.getControllerManager().sendInterfaces();
	}

	public void replaceRealChatBoxInterface(final int interfaceId) {
		player.getPackets().sendInterface(true, 752, 11, interfaceId);
	}

	public void closeReplacedRealChatBoxInterface() {
		player.getPackets().closeInterface(752, 11);
	}

	public void sendWindowPane() {
		player.getPackets().sendWindowsPane(resizableScreen ? 746 : 548, 0);
	}

	public void sendFullScreenInterfaces() {
		sendSquealOfFortune();
		player.getPackets().sendWindowsPane(746, 0);
		sendTab(21, 752);
		sendTab(22, 751);
		sendTab(15, 745);
		sendTab(25, 754);
		sendTab(195, 748);
		sendTab(196, 749);
		sendTab(197, 750);
		sendTab(198, 747);
		player.getPackets().sendInterface(true, 752, 9, 137);
		if (player.getSpins() == 0) {
			player.getPackets().closeInterface(
					player.getInterfaceManager().hasRezizableScreen() ? 11 : 0);
		} else {
			player.getInterfaceManager().sendTab(
					player.getInterfaceManager().hasRezizableScreen() ? 11 : 0,
					1252);
		}
		sendCombatStyles();
		sendQuestTab();
		sendSkills();
		sendActionSystem();
		sendInventory();
		sendEquipment();
		sendPrayerBook();
		sendMagicBook();
		sendTab(119, 1139);
		sendTab(120, 550); // friend list
		sendTab(121, 1109); // 551 ignore now friendchat
		sendTab(122, 1110); // 589 old clan chat now new clan chat
		sendSettings();
		sendEmotes();
		sendTab(125, 187); // music
		sendTab(126, 34); // notes
		sendTab(129, 182); // logout*/
	}

	public void sendFixedInterfaces() {
		player.getPackets().sendWindowsPane(548, 0);
		sendTab(161, 752);
		sendTab(37, 751);
		sendTab(23, 745);
		sendTab(25, 754);
		sendTab(155, 747);
		sendTab(151, 748);
		sendTab(152, 749);
		sendTab(153, 750);
		player.getPackets().sendInterface(true, 752, 9, 137);
		if (player.getSpins() == 0) {
			player.getPackets().closeInterface(
					player.getInterfaceManager().hasRezizableScreen() ? 11 : 0);
		} else {
			player.getInterfaceManager().sendTab(
					player.getInterfaceManager().hasRezizableScreen() ? 11 : 0,
					1252);
		}
		sendMagicBook();
		sendPrayerBook();
		sendEquipment();
		sendInventory();
		sendTab(179, 1139);
		sendActionSystem();
		sendTab(181, 1109);// 551 ignore now friendchat
		sendTab(182, 1110);// 589 old clan chat now new clan chat
		sendTab(180, 550);// friend list
		sendTab(185, 187);// music
		sendTab(186, 34); // notes
		sendTab(189, 182);
		sendSkills();
		sendEmotes();
		sendSettings();
		sendQuestTab();
		sendCombatStyles();
	}

	public void sendXPPopup() {
		sendTab(resizableScreen ? 38 : 10, 1213); // xp
	}

	public void sendXPDisplay() {
		sendXPDisplay(1215); // xp counter
	}

	public void sendXPDisplay(final int interfaceId) {
		sendTab(resizableScreen ? 27 : 29, interfaceId); // xp counter
	}

	public void closeXPPopup() {
		player.getPackets().closeInterface(resizableScreen ? 38 : 10);
	}

	public void sendSquealOfFortune() {
		sendTab(resizableScreen ? 119 : 179, 1139);
		player.getPackets().sendGlobalConfig(823, 1);
	}

	public void closeXPDisplay() {
		player.getPackets().closeInterface(resizableScreen ? 27 : 29);
	}

	public void sendEquipment() {
		sendTab(resizableScreen ? 116 : 176, 387);
	}

	public void closeInterface(final int one, final int two) {
		player.getPackets().closeInterface(resizableScreen ? two : one);
	}

	public void closeEquipment() {
		player.getPackets().closeInterface(resizableScreen ? 116 : 176);
	}

	public void sendInventory() {
		sendTab(resizableScreen ? 115 : 175, Inventory.INVENTORY_INTERFACE);
	}

	public void closeInventory() {
		player.getPackets().closeInterface(resizableScreen ? 115 : 175);
	}

	public void closeSkills() {
		player.getPackets().closeInterface(resizableScreen ? 113 : 206);
	}

	public void closeCombatStyles() {
		player.getPackets().closeInterface(resizableScreen ? 111 : 204);
	}

	public void closeTaskSystem() {
		player.getPackets().closeInterface(resizableScreen ? 112 : 205);
	}

	public void sendCombatStyles() {
		sendTab(resizableScreen ? 111 : 171, 884);
	}

	public void sendQuestTab() {
		sendTab(resizableScreen ? 114 : 174, 930); //112, 172
		QuestTabUpdateTask.sendQuestTab(player);
	}

	public void sendActionSystem() {
		sendTab(resizableScreen ? 112 : 172, 506);
	}

	public void sendOldTaskSystem() {
		sendTab(resizableScreen ? 112 : 172, 551);
		player.getPackets().sendIComponentText(551, 6, "Statistics");
		player.getPackets().sendIComponentText(551, 15, "Vote Points");
		player.getPackets().sendIComponentText(551, 41, "PvM Points");
		player.getPackets().sendIComponentText(551, 55, "Killstreak");
		player.getPackets().sendIComponentText(551, 69, "Coming Soon");
		player.getPackets().sendIComponentText(551, 56, "Killstreak");
		player.getPackets().sendIComponentText(551, 42,
				"" + player.getPlayerPoints().getPoints(PlayerPoints.VOTE_POINTS) + "");
		player.getPackets().sendIComponentText(551, 16, "" + player.getPlayerPoints().getPoints(PlayerPoints.VOTE_POINTS) + "");
		player.getPackets().sendIComponentText(551, 69, "Loyalty Points");
		player.getPackets().sendIComponentText(551, 70,
				"" + player.getPlayerPoints().getPoints(PlayerPoints.LOYALTY_POINTS) + "");
		player.getPackets().sendIComponentText(551, 6, Server.getInstance().getSettingsManager().getSettings().getServerName());
		player.getPackets().sendIComponentText(551, 22, Server.getInstance().getSettingsManager().getSettings().getServerName());
	}

	public void sendSkills() {
		sendTab(resizableScreen ? 113 : 173, 320);
	}

	public void sendSettings() {
		sendSettings(261);
	}

	public void sendSettings(final int interfaceId) {
		sendTab(resizableScreen ? 123 : 183, interfaceId);
	}

	public void sendPrayerBook() {
		sendTab(resizableScreen ? 117 : 177, 271);
	}

	public void closePrayerBook() {
		player.getPackets().closeInterface(resizableScreen ? 117 : 210);
	}

	public void sendMagicBook() {
		sendTab(resizableScreen ? 118 : 178, player.getCombatDefinitions()
				.getSpellBook());
	}

	public void closeMagicBook() {
		player.getPackets().closeInterface(resizableScreen ? 118 : 211);
	}

	public void sendEmotes() {
		sendTab(resizableScreen ? 124 : 184, 590);
	}

	public void closeEmotes() {
		player.getPackets().closeInterface(resizableScreen ? 124 : 217);
	}

	public boolean addInterface(final int windowId, final int tabId,
			final int childId) {
		if (OPENED_INTERFACES.containsKey(tabId)) {
			player.getPackets().closeInterface(tabId);
		}
		OPENED_INTERFACES.put(tabId, new int[]{childId, windowId});
		return OPENED_INTERFACES.get(tabId)[0] == childId;
	}

	public boolean containsInterface(final int tabId, final int childId) {
		return childId == windowsPane || OPENED_INTERFACES.containsKey(tabId) && OPENED_INTERFACES.get(tabId)[0] == childId;
	}

	public int getTabWindow(final int tabId) {
		if (!OPENED_INTERFACES.containsKey(tabId))
			return FIXED_WINDOW_ID;
		return OPENED_INTERFACES.get(tabId)[1];
	}

	public boolean containsInterface(final int childId) {
		if (childId == windowsPane)
			return true;
		for (final int[] value : OPENED_INTERFACES.values())
			if (value[0] == childId)
				return true;
		return false;
	}

	public boolean containsTab(final int tabId) {
		return OPENED_INTERFACES.containsKey(tabId);
	}

	public void removeAll() {
		OPENED_INTERFACES.clear();
	}

	public boolean containsScreenInter() {
		return containsTab(resizableScreen ? RESIZABLE_SCREEN_TAB_ID
				: FIXED_SCREEN_TAB_ID);
	}

	public void closeScreenInterface() {
		player.getPackets()
				.closeInterface(
						resizableScreen ? RESIZABLE_SCREEN_TAB_ID
								: FIXED_SCREEN_TAB_ID);
	}

	public boolean containsInventoryInter() {
		return containsTab(resizableScreen ? RESIZABLE_INV_TAB_ID
				: FIXED_INV_TAB_ID);
	}

	public void closeInventoryInterface() {
		player.getPackets().closeInterface(
				resizableScreen ? RESIZABLE_INV_TAB_ID : FIXED_INV_TAB_ID);
	}

	public boolean containsChatBoxInter() {
		return containsTab(CHAT_BOX_TAB);
	}

	public boolean removeTab(final int tabId) {
		return OPENED_INTERFACES.remove(tabId) != null;
	}

	public boolean removeInterface(final int tabId, final int childId) {
		return OPENED_INTERFACES.containsKey(tabId) && OPENED_INTERFACES.get(tabId)[0] == childId && OPENED_INTERFACES.remove(tabId) != null;
	}

	public void sendFadingInterface(final int backgroundInterface) {
		if (hasRezizableScreen()) {
			player.getPackets().sendInterface(true, RESIZABLE_WINDOW_ID, 12,
					backgroundInterface);
		} else {
			player.getPackets().sendInterface(true, FIXED_WINDOW_ID, 11,
					backgroundInterface);
		}
	}

	public void closeFadingInterface() {
		if (hasRezizableScreen()) {
			player.getPackets().closeInterface(12);
		} else {
			player.getPackets().closeInterface(11);
		}
	}

	public void sendScreenInterface(final int backgroundInterface,
			final int interfaceId) {
		player.getInterfaceManager().closeScreenInterface();

		if (hasRezizableScreen()) {
			player.getPackets().sendInterface(false, RESIZABLE_WINDOW_ID, 40,
					backgroundInterface);
			player.getPackets().sendInterface(false, RESIZABLE_WINDOW_ID, 41,
					interfaceId);
		} else {
			player.getPackets().sendInterface(false, FIXED_WINDOW_ID, 200,
					backgroundInterface);
			player.getPackets().sendInterface(false, FIXED_WINDOW_ID, 201,
					interfaceId);

		}

		player.setCloseInterfacesEvent(() -> {
			if (hasRezizableScreen()) {
				player.getPackets().closeInterface(40);
				player.getPackets().closeInterface(41);
			} else {
				player.getPackets().closeInterface(200);
				player.getPackets().closeInterface(201);
			}
		});
	}

	public boolean hasRezizableScreen() {
		return resizableScreen;
	}

	public int getWindowsPane() {
		return windowsPane;
	}

	public void setWindowsPane(final int windowsPane) {
		this.windowsPane = windowsPane;
	}

	public void gazeOrbOfOculus() {
		player.getPackets().sendWindowsPane(475, 0);
		player.getPackets().sendInterface(true, 475, 57, 751);
		player.getPackets().sendInterface(true, 475, 55, 752);
		player.setCloseInterfacesEvent(() -> {
			player.getPackets().sendWindowsPane(
					player.getInterfaceManager().hasRezizableScreen() ? 746
							: 548, 0);
			player.getPackets().sendResetCamera();
		});
	}

	/*
	 * returns lastGameTab
	 */
	public int openGameTab(final int tabId) {
		player.getPackets().sendGlobalConfig(168, tabId);
		return 4;
	}

}