package com.rs.content.staff.actions.impl;

import com.rs.content.staff.actions.StaffAction;
import com.rs.content.player.PlayerRank;
import com.rs.world.World;
import com.rs.world.WorldObject;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
public class SpawnObjectAction implements StaffAction {

    @Override
    public void handle(Player player, String value) {
        try {
            World.spawnObject(
                    new WorldObject(Integer.valueOf(value), 10, -1,
                            player.getX(), player.getY(), player
                            .getPlane()), true);
        } catch (final NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PlayerRank getMinimumRights() {
        return PlayerRank.ADMIN;
    }
}
