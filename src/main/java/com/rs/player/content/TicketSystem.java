package com.rs.player.content;

import com.rs.content.player.PlayerRank;
import com.rs.player.Player;
import com.rs.world.ForceTalk;
import com.rs.world.World;
import com.rs.world.WorldTile;

import java.util.ArrayList;
import java.util.Iterator;

public class TicketSystem {

	public static final ArrayList<TicketEntry> tickets = new ArrayList<TicketEntry>();

	public static boolean canSubmitTicket() {
		filterTickets();
		return true;
	}

	public static void filterTickets() {
		for (final Iterator<TicketEntry> it = tickets.iterator(); it.hasNext();) {
			final TicketEntry entry = it.next();
			if (entry.player.hasFinished()) {
				it.remove();
			}
		}
	}

	public static void removeTicket(final Player player) {
		final Object att = player.getTemporaryAttributtes().get("ticketTarget");
		if (att == null)
			return;
		final TicketEntry ticket = (TicketEntry) att;
		final Player target = ticket.getPlayer();
		target.setNextWorldTile(ticket.getTile());
		target.getTemporaryAttributtes().remove("ticketRequest");
		player.getTemporaryAttributtes().remove("ticketTarget");
	}

	public static void answerTicket(final Player player) {
		removeTicket(player);
		filterTickets();
		if (tickets.isEmpty()) {
			player.getPackets().sendGameMessage(
					"There are no tickets open, congratulations! Good job!");
			return;
		} else if (player.getTemporaryAttributtes().get("ticketTarget") != null) {
			removeTicket(player);
		}
		while (tickets.size() > 0) {
			final TicketEntry ticket = tickets.get(0);// next in line
			final Player target = ticket.player;
			if (target == null) {
				continue; // shouldn't happen but k
			}
			if (target.getInterfaceManager().containsChatBoxInter()
					|| target.getControllerManager().getController() != null
					|| target.getInterfaceManager().containsInventoryInter()
					|| target.getInterfaceManager().containsScreenInter()) {
				tickets.remove(0);
				continue;
			}
			player.getTemporaryAttributtes().put("ticketTarget", ticket);
			target.setNextWorldTile(player);
			tickets.remove(ticket);
			player.setNextForceTalk(new ForceTalk(
					"Hello, how may I help you today?"));
			break;
		}
	}

	public static void requestTicket(final Player player) {
		if (player.getInterfaceManager().containsChatBoxInter()
				|| player.getInterfaceManager().containsInventoryInter()
				|| player.getInterfaceManager().containsScreenInter()) {
			player.getPackets()
					.sendGameMessage(
							"Please finish what you're doing before requesting a ticket.");
			return;
		}
		if (!canSubmitTicket()
				|| player.getTemporaryAttributtes().get("ticketRequest") != null
				|| player.getControllerManager().getController() != null) {
			player.getPackets()
					.sendGameMessage("You cannot send a ticket yet!");
			return;
		}
		player.getTemporaryAttributtes().put("ticketRequest", true);
		tickets.add(new TicketEntry(player));
		for (final Player mod : World.getPlayers()) {
			if (mod == null || mod.hasFinished() || !mod.hasStarted()
					|| (mod.getRank().isMinimumRank(PlayerRank.MOD))) {
				continue;
			}
			mod.getPackets().sendGameMessage(
					"A ticket has been submitted by " + player.getDisplayName()
							+ "! Click the J-Mod table to accept it.");
			mod.getPackets()
					.sendGameMessage(
							"There is currently " + tickets.size()
									+ " tickets active.");
		}
	}

	public static class TicketEntry {
		private final Player player;
		private final WorldTile tile;

		public TicketEntry(final Player player) {
			this.player = player;
			this.tile = player;
		}

		public Player getPlayer() {
			return player;
		}

		public WorldTile getTile() {
			return tile;
		}
	}
}
