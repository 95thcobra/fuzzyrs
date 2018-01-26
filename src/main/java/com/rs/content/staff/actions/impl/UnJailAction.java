package com.rs.content.staff.actions.impl;

import com.rs.Server;
import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffAction;
import com.rs.core.settings.SettingsManager;
import com.rs.player.Player;
import com.rs.player.controlers.JailController;
import com.rs.world.World;

/**
 * @author FuzzyAvacado
 */
public class UnJailAction implements StaffAction {

    @Override
    public void handle(Player player, String value) {
        Player target = World.getPlayerByDisplayName(value);
        if (target != null) {
            target.setJailed(0);
            JailController.stopControler(target);
            target.setNextWorldTile(Server.getInstance().getSettingsManager().getSettings().getRespawnPlayerLocation());
            target.getPackets()
                    .sendGameMessage("You've been unjailed.");
            player.getPackets().sendGameMessage(
                    "You have unjailed " + target.getDisplayName()
                            + ".");
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
