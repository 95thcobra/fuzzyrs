package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;
import com.rs.world.World;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "givespins", rank = PlayerRank.ADMIN)
public class GiveSpinsCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        final String username = cmd[1]
                .substring(cmd[1].indexOf(" ") + 1);
        final Player other = World.getPlayerByDisplayName(username);
        if (other == null)
            return;
        other.setSpins(Integer.parseInt(cmd[2]));
        other.getPackets().sendGameMessage(
                "<col=ff0000>You have recieved some SoF Spins!</col>");
        other.getPackets()
                .sendGameMessage(
                        "<col=ff0000>Pictures are mixed up Claim all Prizes!</col>");
        player.getPackets().sendGameMessage(
                "You have given SoF Spins to " + other.getDisplayName()
                        + ".");
    }
}
