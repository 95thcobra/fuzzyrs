package com.rs.content.staff.actions.impl;

import com.rs.content.staff.actions.StaffAction;
import com.rs.content.player.PlayerRank;
import com.rs.world.World;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
public class SpawnNpcAction implements StaffAction {

    @Override
    public void handle(Player player, String value) {
        try {
            World.spawnNPC(Integer.parseInt(value), player, -1, true,
                    true);
        } catch (final NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PlayerRank getMinimumRights() {
        return PlayerRank.ADMIN;
    }
}
