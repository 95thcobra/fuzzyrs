package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.core.settings.SettingsManager;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "unstuck", rank = PlayerRank.ADMIN)
public class UnstuckPlayerCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.setNextWorldTile(SettingsManager.getSettings().RESPAWN_PLAYER_LOCATION);
    }
}
