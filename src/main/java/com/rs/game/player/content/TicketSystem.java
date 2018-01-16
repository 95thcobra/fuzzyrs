package com.rs.game.player.content;

import java.util.ArrayList;
import java.util.Iterator;



import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;


public class TicketSystem {


	public static final ArrayList<TicketEntry> tickets = new ArrayList<TicketEntry>();

	public static boolean canSubmitTicket() {
		filterTickets();
		return true;
	}

	public static void filterTickets() {
		for (Iterator<TicketEntry> it = tickets.iterator(); it.hasNext(); ) {
			TicketEntry entry = it.next();
			if(entry.player.hasFinished())
				it.remove();
		}
	}

	public static void removeTicket(Player player) {
		Object att = player.getTemporaryAttributtes().get("ticketTarget ");
		if (att == null) return;
		TicketEntry ticket = (TicketEntry) att;
		Player target = ticket.getPlayer();
		target.setNextWorldTile(ticket.getTile());
		target.getTemporaryAttributtes().remove("ticketRequest");
		player.getTemporaryAttributtes().remove("ticketTarget");
	}

	public static void answerTicket(Player player) {
		removeTicket(player);
		filterTickets();
		if (tickets.isEmpty()) {
			player.getPackets().sendGameMessage("There aren't any active tickets at the moment.");
			return;
		} else if (player.getTemporaryAttributtes().get("ticketTarget") != null) {
			removeTicket(player);
		}
		while(tickets.size() > 0) {
			TicketEntry ticket = tickets.get(0);
			Player target = ticket.player;
			if (target == null) 
				continue;
			if(target.getInterfaceManager().containsChatBoxInter()
					|| target.getControlerManager().getControler() != null
					|| target.getInterfaceManager().containsInventoryInter()
					|| target.getInterfaceManager().containsScreenInter() ) {
				tickets.remove(0);
				continue;
			}
			player.getTemporaryAttributtes().put("ticketTarget ", ticket);
			target.setNextWorldTile(player);
			tickets.remove(ticket);
			player.setTicketCount(player.getTicketCount() + 1);
			player.setNextForceTalk(new ForceTalk("Hello, " + target.getDisplayName() + ". How may I assist you today?"));
			World.sendWorldMessage("[<col=F20505>Ticket</col>] <col=F20505>" + player.getDisplayName() + "</col> has taken <col=F20505>" + target.getDisplayName() + "'s</col> ticket.", true);
			break;
		}
	}

	public static void requestTicket(Player player) {
		if(player.getInterfaceManager().containsChatBoxInter()
				|| player.getInterfaceManager().containsInventoryInter()
				|| player.getInterfaceManager().containsScreenInter() ) {
			player.getPackets().sendGameMessage(
					"Please finish what you're doing before requesting a ticket.");
			return;
		}
		if(!canSubmitTicket() || player.getTemporaryAttributtes().get("ticketRequest") != null || player.getControlerManager().getControler() != null) {
			player.getPackets().sendGameMessage("You can't submit a ticket yet.");
			return;
		}
		player.getTemporaryAttributtes().put("ticketRequest", true);
		tickets.add(new TicketEntry(player));
		for(Player mod : World.getPlayers()) {
			if(mod == null || mod.hasFinished() || !mod.hasStarted() || (mod.getRights() < 1 ))
				continue;
			player.getPackets().sendGameMessage("Your ticket has been submitted... Please wait patiently.");
			mod.getPackets().sendGameMessage("[<col=F20505>Ticket</col>] A ticket has been submitted by <col=F20505>" + player.getDisplayName() + "</col>. Use ::ticket to solve it.");
			mod.getPackets().sendGameMessage("<col=F20505>There are "+tickets.size()+" active tickets.");
		}
	}

	public static class TicketEntry {
		private Player player;
		private WorldTile tile;

		public TicketEntry(Player player) {
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
