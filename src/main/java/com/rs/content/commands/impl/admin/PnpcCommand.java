package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "pnpc", rank = PlayerRank.ADMIN)
public class PnpcCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        if (cmd.length < 2) {
            player.getPackets().sendPanelBoxMessage(
                    "Use: ::pnpc id(-1 for player)");
            return;
        }
        try {
            player.getAppearance().transformIntoNPC(
                    Integer.valueOf(cmd[1]));
        } catch (final NumberFormatException e) {
            player.getPackets().sendPanelBoxMessage(
                    "Use: ::pnpc id(-1 for player)");
        }
    }
}
