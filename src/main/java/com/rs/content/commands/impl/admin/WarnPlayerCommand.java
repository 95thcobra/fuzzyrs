package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;
import com.rs.world.World;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "warn", rank = PlayerRank.ADMIN)
public class WarnPlayerCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        final String name = cmd[1].substring(cmd[1].indexOf(" ") + 1);
        final Player others = World.getPlayerByDisplayName(name);
        if (others == null)
            return;
        others.setWarned(others.getWarned() + 1);
        others.getPackets()
                .sendGameMessage(
                        "<col=ff0000>You are being observed by a staff member.</col>");
        others.getPackets().sendGameMessage(
                "<col=ff0000>You have been warned " + others.getWarned()
                        + " times.</col>");
    }
}
