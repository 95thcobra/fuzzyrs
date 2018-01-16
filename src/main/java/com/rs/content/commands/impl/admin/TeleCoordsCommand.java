package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;
import com.rs.world.WorldTile;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "tele", rank = PlayerRank.ADMIN)
public class TeleCoordsCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        if (cmd.length < 3) {
            player.getPackets().sendPanelBoxMessage(
                    "Use: ::tele coordX coordY");
            return;
        }
        try {
            player.resetWalkSteps();
            player.setNextWorldTile(new WorldTile(Integer
                    .valueOf(cmd[1]), Integer.valueOf(cmd[2]),
                    cmd.length >= 4 ? Integer.valueOf(cmd[3]) : player
                            .getPlane()));
        } catch (final NumberFormatException e) {
            player.getPackets().sendPanelBoxMessage(
                    "Use: ::tele coordX coordY plane");
        }
    }
}
