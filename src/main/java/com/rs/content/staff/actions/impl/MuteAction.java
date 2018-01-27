package com.rs.content.staff.actions.impl;

import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffAction;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.World;

/**
 * @author FuzzyAvacado
 */
public class MuteAction implements StaffAction {

    @Override
    public void handle(Player player, String value) {
        Player target = World.getPlayerByDisplayName(value);
        if (target != null) {
            target.setMuted(Utils.currentTimeMillis()
                    + (48 * 60 * 60 * 1000));
            target.getPackets().sendGameMessage(
                    "You've been muted for 48 hours.");
            player.getPackets().sendGameMessage(
                    "You have muted 48 hours: "
                            + target.getDisplayName() + ".");
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
