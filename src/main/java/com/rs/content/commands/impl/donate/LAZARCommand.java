package com.rs.content.commands.impl.donate;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "lazar", donateRank = PlayerRank.DonateRank.SUPER_DONATOR)
public class LAZARCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {

    }
}
