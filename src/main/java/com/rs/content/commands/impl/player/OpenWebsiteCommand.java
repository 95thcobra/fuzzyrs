package com.rs.content.commands.impl.player;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.core.settings.SettingsManager;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "website")
public class OpenWebsiteCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.getPackets().sendOpenURL(SettingsManager.getSettings().VOTE_LINK);
    }
}
