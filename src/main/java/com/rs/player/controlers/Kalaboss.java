package com.rs.player.controlers;

import com.rs.player.Player;
import com.rs.world.WorldTile;

public class Kalaboss extends Controller {

	private boolean showingOption;

	public static boolean isAtKalaboss(final WorldTile tile) {
		return tile.getX() >= 3385 && tile.getX() <= 3513
				&& tile.getY() >= 3605 && tile.getY() <= 3794;
	}

	@Override
	public void start() {
		setInviteOption(true);
	}

	@SuppressWarnings("unused")
	@Override
	public boolean canPlayerOption1(final Player target) {
		return true;
		//player.setNextFaceWorldTile(target);
		//player.getPackets().sendGameMessage("You can't do that right now.");
		//return false;
	}

	@Override
	public boolean login() {
		moved();
		return false;
	}

	@Override
	public boolean sendDeath() {
		setInviteOption(false);
		removeControler();
		return true;
	}

	@Override
	public boolean logout() {
		return false; // so doesnt remove script
	}

	@Override
	public void forceClose() {
		setInviteOption(false);
	}

	@Override
	public void moved() {
		if (player.getX() == 3385 && player.getY() == 3615) {
			setInviteOption(false);
			removeControler();
			player.getControllerManager().startController(Wilderness.class);
		} else {
			if (!isAtKalaboss(player)) {
				setInviteOption(false);
				removeControler();
			} else {
				setInviteOption(true);
			}
		}
	}

	public void setInviteOption(final boolean show) {
		if (show == showingOption)
			return;
		showingOption = show;
		player.getPackets()
		.sendPlayerOption(show ? "Invite" : "null", 1, false);
	}
}
