package com.rs.content.commands.impl.player;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.player.Player;
import com.rs.world.ForceTalk;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "mytask")
public class MyTaskCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        if (player.getTask() != null) {
            player.setNextForceTalk(new ForceTalk("<col=ff0000>MY TASK IS TO KILL " + player.getTask().getTaskAmount() + " "
                    + player.getTask().getName().toLowerCase() + "s."));
        }
    }
}
