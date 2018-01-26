package com.rs.content.commands.impl.admin;

import com.rs.Server;
import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.core.settings.SettingsManager;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.ForceTalk;
import com.rs.world.World;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "bowall", rank = PlayerRank.ADMIN)
public class BowPlayersCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        for (final Player players : World.getPlayers()) {
            if (players == null) {
                continue;
            }
            players.setNextAnimation(new Animation(9098));
            players.setNextForceTalk(new ForceTalk(
                    "We hail the great owner of "
                            + Server.getInstance().getSettingsManager().getSettings().getServerName()));
        }
    }
}
