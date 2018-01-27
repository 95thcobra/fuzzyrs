package com.rs.content.staff.actions.impl;

import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffAction;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.player.controlers.JailController;
import com.rs.world.World;

/**
 * @author FuzzyAvacado
 */
public class JailAction implements StaffAction {

    @Override
    public void handle(Player player, String value) {
        Player target = World.getPlayerByDisplayName(value);
        if (target != null) {
            target.setJailed(Utils.currentTimeMillis()
                    + (24 * 60 * 60 * 1000));
            target.getControllerManager()
                    .startController(JailController.class);
            target.getPackets().sendGameMessage(
                    "You've been jailed for 24 hours.");
            player.getPackets().sendGameMessage(
                    "You have jailed 24 hours: "
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
