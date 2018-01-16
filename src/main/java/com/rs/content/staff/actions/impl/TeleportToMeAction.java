package com.rs.content.staff.actions.impl;

import com.rs.content.staff.actions.StaffAction;
import com.rs.content.player.PlayerRank;
import com.rs.world.World;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
public class TeleportToMeAction implements StaffAction {

    @Override
    public void handle(Player player, String value) {
        Player target = World.getPlayerByDisplayName(value);
        if (target == null) {
            return;
        }
        target.setNextWorldTile(player);
        target.stopAll();
    }

    @Override
    public PlayerRank getMinimumRights() {
        return PlayerRank.ADMIN;
    }
}
