package com.rs.content.commands.impl.mod;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;
import com.rs.player.content.TicketSystem;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "answerticket", rank = PlayerRank.MOD)
public class AnswerTicketCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        TicketSystem.answerTicket(player);
    }
}
