package com.rs.content.actions.impl;

import com.rs.content.actions.Action;
import com.rs.world.WorldTile;
import com.rs.player.Player;

public class FightPitsViewingOrb extends Action {

	public static final WorldTile[] ORB_TELEPORTS = {
			new WorldTile(4571, 5092, 0), new WorldTile(4571, 5107, 0),
			new WorldTile(4590, 5092, 0), new WorldTile(4571, 5077, 0),
		new WorldTile(4557, 5092, 0) };

	private WorldTile tile;

	@Override
	public boolean start(final Player player) {
		if (!process(player))
			return false;
		tile = new WorldTile(player);
		player.getAppearance().switchHidden();
		player.getPackets().sendBlackOut(5);
		player.setNextWorldTile(ORB_TELEPORTS[0]);
		player.getInterfaceManager().sendInventoryInterface(374);
		return true;
	}

	@Override
	public boolean process(final Player player) {
		if (player.getPoison().isPoisoned()) {
			player.getPackets().sendGameMessage(
					"You can't use orb while you're poisoned.");
			return false;
		}
		if (player.getFamiliar() != null) {
			player.getPackets().sendGameMessage(
					"You can't use orb with a familiar.");
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(final Player player) {
		return 0;
	}

	@Override
	public void stop(final Player player) {
		player.lock(2);
		player.getInterfaceManager().closeInventoryInterface();
		player.getAppearance().switchHidden();
		player.getPackets().sendBlackOut(0);
		player.setNextWorldTile(tile);
	}

}
