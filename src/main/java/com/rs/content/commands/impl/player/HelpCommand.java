package com.rs.content.commands.impl.player;

import com.rs.Server;
import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.core.settings.SettingsManager;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "help")
public class HelpCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.getInventory().addItem(1856, 1);
        player.getPackets().sendGameMessage("You receive a guide book about " + Server.getInstance().getSettingsManager().getSettings().getServerName() + ".");
    }
}
