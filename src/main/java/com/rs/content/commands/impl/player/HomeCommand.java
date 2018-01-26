package com.rs.content.commands.impl.player;

import com.rs.server.Server;
import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.player.Player;
import com.rs.player.content.Magic;

/**
 * @author John (FuzzyAvacado) on 1/2/2016.
 */
@CommandInfo(name = "home")
public class HomeCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        Magic.sendNormalTeleportSpell(player, 0, 0, Server.getInstance().getSettingsManager().getSettings().getStartPlayerLocation());
    }
}
