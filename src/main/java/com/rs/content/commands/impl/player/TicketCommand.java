package com.rs.content.commands.impl.player;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.player.Player;
import com.rs.player.content.TicketSystem;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "ticket")
public class TicketCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        TicketSystem.requestTicket(player);
    }
}
