package com.rs.content.commands.impl.owner;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;
import com.rs.world.World;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "teleall", rank = PlayerRank.OWNER)
public class TeleportAllCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        for (Player players : World.getPlayers()) {
            if (players == null)
                continue;
            players.setNextWorldTile(player);
            players.getPackets().sendGameMessage(
                    "You have been teleported here by "
                            + player.getDisplayName());
        }
    }
}
