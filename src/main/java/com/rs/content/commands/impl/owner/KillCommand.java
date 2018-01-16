package com.rs.content.commands.impl.owner;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;
import com.rs.world.Hit;
import com.rs.world.World;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "kill", rank = PlayerRank.OWNER)
public class KillCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
        Player other = World.getPlayerByDisplayName(username);
        if (other == null)
            return;
        other.applyHit(new Hit(other, player.getHitpoints(),
                Hit.HitLook.REGULAR_DAMAGE));
        other.stopAll();
    }
}
