package com.rs.content.commands.impl.player;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "changetitle")
public class ChangeTitleCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        final int string = Integer.parseInt(cmd[1]);
        player.setCustomTitle(player.getCustomTitle() + string);
        player.getPackets().sendGameMessage("You changed title to " + string + "!");
    }
}
