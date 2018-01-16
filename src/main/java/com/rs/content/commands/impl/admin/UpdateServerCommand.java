package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;
import com.rs.world.World;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "update", rank = PlayerRank.OWNER)
public class UpdateServerCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        int delay = 60;
        if (cmd.length >= 2) {
            try {
                delay = Integer.valueOf(cmd[1]);
            } catch (final NumberFormatException e) {
                player.getPackets().sendPanelBoxMessage(
                        "Use: ::update secondsDelay(IntegerValue)");
                return;
            }
        }
        World.safeShutdown(true, delay);
    }
}
