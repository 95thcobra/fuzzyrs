package com.rs.content.commands.impl.player;

import com.rs.Server;
import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.core.settings.SettingsManager;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "vote")
public class VoteCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.getPackets().sendOpenURL(Server.getInstance().getSettingsManager().getSettings().getVoteLink());
    }
}
