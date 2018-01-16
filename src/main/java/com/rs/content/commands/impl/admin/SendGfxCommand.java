package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;
import com.rs.world.Graphics;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "gfx", rank = PlayerRank.ADMIN)
public class SendGfxCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        if (cmd.length < 2) {
            player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
            return;
        }
        try {
            player.setNextGraphics(new Graphics(Integer.valueOf(cmd[1])));
        } catch (final NumberFormatException e) {
            player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
        }
    }
}
