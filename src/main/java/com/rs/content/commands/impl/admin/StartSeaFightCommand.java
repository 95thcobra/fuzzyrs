package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;

/**
 * @author John (FuzzyAvacado) on 12/12/2015.
 */
@CommandInfo(name = "seafight", rank = PlayerRank.ADMIN)
public class StartSeaFightCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {

    }
}
