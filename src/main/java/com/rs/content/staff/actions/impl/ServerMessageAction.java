package com.rs.content.staff.actions.impl;

import com.rs.content.staff.actions.StaffAction;
import com.rs.content.player.PlayerRank;
import com.rs.world.World;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
public class ServerMessageAction implements StaffAction {

    private static final String PART_ONE = "<img=1><col=008000>[SERVER MESSAGE]:</col></img><col=008000> ";
    private static final String END_COLOR = "</col>";

    @Override
    public void handle(Player player, String value) {
        World.sendGlobalMessage(PART_ONE + value + END_COLOR);
    }

    @Override
    public PlayerRank getMinimumRights() {
        return PlayerRank.ADMIN;
    }
}
