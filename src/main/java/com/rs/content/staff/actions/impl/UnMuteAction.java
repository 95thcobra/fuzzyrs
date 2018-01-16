package com.rs.content.staff.actions.impl;

import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffAction;
import com.rs.player.Player;
import com.rs.world.World;

/**
 * @author FuzzyAvacado
 */
public class UnMuteAction implements StaffAction {

    @Override
    public void handle(Player player, String value) {
        Player target = World.getPlayerByDisplayName(value);
        if (target != null) {
            target.setMuted(0);
            player.getPackets().sendGameMessage(
                    "You have unmuted: " + target.getDisplayName()
                            + ".");
            target.getPackets().sendGameMessage(
                    "You have been unmuted by : "
                            + player.getUsername());
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
