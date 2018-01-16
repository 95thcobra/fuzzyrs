package com.rs.content.commands.impl.owner;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;
import com.rs.world.World;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "changepassother", rank = PlayerRank.OWNER)
public class ChangePasswordOtherCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        final String username = cmd[1]
                .substring(cmd[1].indexOf(" ") + 1);
        final Player other = World.getPlayerByDisplayName(username);
        if (other == null)
            return;
        other.setPassword(cmd[2]);
        player.getPackets().sendGameMessage(
                "You changed their password!");
    }
}
