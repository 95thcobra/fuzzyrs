package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "tab", rank = PlayerRank.ADMIN)
public class OpenTabCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.getInterfaceManager().sendTab(Integer.valueOf(cmd[2]), Integer.valueOf(cmd[1]));
    }
}
