package com.rs.content.staff.actions.impl;

import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffAction;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.server.Server;
import com.rs.world.World;

import java.util.Optional;

/**
 * @author FuzzyAvacado
 */
public class BanAction implements StaffAction {

    @Override
    public void handle(Player player, String value) {
        Player target = World.getPlayerByDisplayName(value);
        boolean loggedIn = true;
        if (target == null) {
            Optional<Player> targetOptional = Server.getInstance().getPlayerFileManager().load(value);
            if (targetOptional.isPresent()) {
                target = targetOptional.get();
                target.setUsername(Utils
                        .formatPlayerNameForProtocol(value));
            }
            loggedIn = false;
        }
        if (target != null) {
            target.setPermBanned(true);
            player.getPackets().sendGameMessage(
                    "You've permanently banned "
                            + (loggedIn ? target.getDisplayName()
                            : value) + ".");
        } else {
            player.getPackets().sendGameMessage(
                    "Couldn't find player " + value + ".");
        }
    }

    @Override
    public PlayerRank getMinimumRights() {
        return PlayerRank.MOD;
    }
}
