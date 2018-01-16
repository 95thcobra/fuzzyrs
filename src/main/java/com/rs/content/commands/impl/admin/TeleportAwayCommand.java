package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.core.settings.SettingsManager;
import com.rs.player.Player;
import com.rs.world.World;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "teleaway", rank = PlayerRank.ADMIN)
public class TeleportAwayCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        final String username = cmd[1]
                .substring(cmd[1].indexOf(" ") + 1);
        final Player other = World.getPlayerByDisplayName(username);
        if (other == null)
            return;
        other.setNextWorldTile(SettingsManager.getSettings().RESPAWN_PLAYER_LOCATION);
        other.stopAll();
    }
}
