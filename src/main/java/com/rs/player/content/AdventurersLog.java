package com.rs.player.content;

import com.rs.player.Player;

public final class AdventurersLog {

	private AdventurersLog() {

	}

	public static void open(final Player player) {
		player.getInterfaceManager().sendInterface(623);
	}
}
