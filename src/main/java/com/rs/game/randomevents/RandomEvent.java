package com.rs.game.randomevents;

import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public abstract class RandomEvent { 
	
	public int[] i = {  1,2,3,4,5};
	
	protected Player player;

	public final void setPlayer(Player player) {
		this.player = player;
	}
	
	public void process() {

	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void forceClose() {
	}

	public final Object[] getArguments() {
		return player.getRandomEventManager().getLastEventParams();
	}

	public final void setArguments(Object[] objects) {
		player.getRandomEventManager().setLastEventParams(objects);
	}

	public final void removeEventWithoutCheck() {
		player.getRandomEventManager().removeEventWithoutCheck();
	}

	public abstract void start();
	
	public abstract void finish();
	
	public boolean processObjectClick1(WorldObject object) {
		return true;
	}

	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		return true;
	}
	
	public boolean processNPCClick1(NPC npc) {
		return true;
	}
	
	public WorldTile getPlayerTiles() {
		return new WorldTile(player.getX() -1, player.getY(), player.getPlane());
	}
	
	private static int[] CHARMS = { 12158, 12159, 12130, 12163 };

	private static int[] SEEDS = { 5305, 5320, 5309, 5300, 5318, 5096 };
	
	private static int[] RUNES = { 560, 562, 558, 559, 561, 564, 563, 554, 557, 556, 555, 7937, 1437 };
	
	private static int[] HERBS = { 212, 216, 218, 206, 210, 214, 208, 204, 2486, 3052 };

	private static int[] BARS = { 2350, 2352, 2356, 2358, 2360, 2362 };

	private static int[] ORES = { 435, 1762, 454, 445, 448, 450, 437, 441, 451 };

	private static int[] GEMS = { 1624, 1622, 1620, 1618, 1607, 1606, 1604, 1602 };

	public static void handleOpenGift(Player player) {
		if (player.getInventory().getFreeSlots() < 8) {
			player.getPackets().sendGameMessage("You need atleast 8 free inventory slots to open the random event gift.");
			return;
		}
		player.getPackets().sendGameMessage("You open the random event gift.");
		player.getInventory().addItem(GEMS[Utils.random(GEMS.length)], 2 + Utils.random(6));
		player.getInventory().addItem(ORES[Utils.random(ORES.length)], 5 + Utils.random(15));
		player.getInventory().addItem(HERBS[Utils.random(HERBS.length)], 4 + Utils.random(8));
		player.getInventory().addItem(BARS[Utils.random(BARS.length)], 3 + Utils.random(4));
		player.getInventory().addItem(RUNES[Utils.random(RUNES.length)], 60 + Utils.random(25));
		player.getInventory().addItem(SEEDS[Utils.random(SEEDS.length)], 5 + Utils.random(10));
		player.getInventory().addItem(CHARMS[Utils.random(CHARMS.length)], 5 + Utils.random(3));
	}
}