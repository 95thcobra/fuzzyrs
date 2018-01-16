package com.rs.player.controlers;

import com.rs.content.minigames.ZarosGodwars;

public class ZGDController extends Controller {

	@Override
	public void start() {
		ZarosGodwars.addPlayer(player);
		sendInterfaces();
	}

	@Override
	public boolean logout() {
		ZarosGodwars.removePlayer(player);
		return false; // so doesnt remove script
	}

	@Override
	public boolean login() {
		ZarosGodwars.addPlayer(player);
		sendInterfaces();
		return false; // so doesnt remove script
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager()
				.sendTab(
						player.getInterfaceManager().hasRezizableScreen() ? 34
								: 8, 601);
	}

	@Override
	public boolean sendDeath() {
		remove();
		removeControler();
		return true;
	}

	@Override
	public void magicTeleported(final int type) {
		remove();
		removeControler();
	}

	@Override
	public void forceClose() {
		remove();
	}

	public void remove() {
		ZarosGodwars.removePlayer(player);
		player.getPackets().closeInterface(
				player.getInterfaceManager().hasRezizableScreen() ? 34 : 8);
	}
}
