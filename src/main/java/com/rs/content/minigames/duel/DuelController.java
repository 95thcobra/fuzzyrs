package com.rs.content.minigames.duel;

import com.rs.player.Player;
import com.rs.player.controlers.Controller;
import com.rs.world.WorldTile;

public class DuelController extends Controller {

	public static void challenge(final Player player) {
		player.closeInterfaces();
		final Boolean friendly = (Boolean) player.getTemporaryAttributtes()
				.remove("WillDuelFriendly");
		if (friendly == null)
			return;
		final Player target = (Player) player.getTemporaryAttributtes().remove(
				"DuelTarget");
		if (target == null
				|| target.hasFinished()
				|| !target.withinDistance(player, 14)
				|| !(target.getControllerManager().getController() instanceof DuelController)) {
			player.getPackets().sendGameMessage(
					"Unable to find "
							+ (target == null ? "your target" : target
							.getDisplayName()));
			return;
		}
		player.getTemporaryAttributtes().put("DuelChallenged", target);
		player.getTemporaryAttributtes().put("DuelFriendly", friendly);
		player.getPackets().sendGameMessage(
				"Sending " + target.getDisplayName() + " a request...");
		target.getPackets().sendDuelChallengeRequestMessage(player, friendly);
	}

	public static boolean isAtDuelArena(final WorldTile player) {
		return (player.getX() >= 3355 && player.getX() <= 3360
				&& player.getY() >= 3267 && player.getY() <= 3279)
				|| (player.getX() >= 3355 && player.getX() <= 3379
				&& player.getY() >= 3272 && player.getY() <= 3279)
				|| (player.getX() >= 3374 && player.getX() <= 3379
				&& player.getY() >= 3267 && player.getY() <= 3271);
	}

	@Override
	public void start() {
		sendInterfaces();
		player.getAppearance().generateAppearenceData();
		player.getPackets().sendPlayerOption("Challenge", 1, false);
		moved();
	}

	@Override
	public boolean login() {
		start();
		return false;
	}

	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public void forceClose() {
		remove();
	}

	@Override
	public boolean processMagicTeleport(final WorldTile toTile) {
		return true;
	}

	@Override
	public boolean processItemTeleport(final WorldTile toTile) {
		return true;
	}

	@Override
	public void magicTeleported(final int type) {
		removeControler();
		remove();
	}

	@Override
	public void moved() {
		if (!isAtDuelArena(player)) {
			removeControler();
			remove();
		}
	}

	@Override
	public boolean canPlayerOption1(final Player target) {
		player.stopAll();
		if (target.getInterfaceManager().containsScreenInter()) {
			player.getPackets().sendGameMessage("The other player is busy.");
			return false;
		}
		if (target.getTemporaryAttributtes().get("DuelChallenged") == player) {
			player.getControllerManager().removeControlerWithoutCheck();
			target.getControllerManager().removeControlerWithoutCheck();
			target.getTemporaryAttributtes().remove("DuelChallenged");
			player.setLastDuelRules(new DuelRules(player, target));
			target.setLastDuelRules(new DuelRules(target, player));
			player.getControllerManager().startController(DuelArena.class, target, target.getTemporaryAttributtes().get("DuelFriendly"));
			target.getControllerManager().startController(DuelArena.class, player, target.getTemporaryAttributtes().remove("DuelFriendly"));
			return false;
		}
		player.getTemporaryAttributtes().put("DuelTarget", target);
		player.getInterfaceManager().sendInterface(640);
		player.getTemporaryAttributtes().put("WillDuelFriendly", true);
		player.getPackets().sendConfig(283, 67108864);
		return false;
	}

	public void remove() {
		player.getPackets().closeInterface(
				player.getInterfaceManager().hasRezizableScreen() ? 11 : 27);
		player.getAppearance().generateAppearenceData();
		player.getPackets().sendPlayerOption("null", 1, false);
	}

	@Override
	public void sendInterfaces() {
		if (isAtDuelArena(player)) {
			player.getInterfaceManager()
					.sendTab(
							player.getInterfaceManager().hasRezizableScreen() ? 11
									: 27, 638);
		}
	}
}
