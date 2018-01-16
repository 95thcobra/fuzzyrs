package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "config", rank = PlayerRank.DEVELOPER)
public class SendConfigCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        if (cmd[0].equalsIgnoreCase("config")) {
            if (cmd.length < 3) {
                player.getPackets().sendPanelBoxMessage(
                        "Use: config id value");
                return;
            }
            try {
                player.getPackets().sendConfig(Integer.valueOf(cmd[1]),
                        Integer.valueOf(cmd[2]));
            } catch (final NumberFormatException e) {
                player.getPackets().sendPanelBoxMessage(
                        "Use: config id value");
            }
        }
    }
}
