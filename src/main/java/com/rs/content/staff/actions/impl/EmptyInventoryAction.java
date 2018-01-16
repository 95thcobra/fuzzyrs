package com.rs.content.staff.actions.impl;

import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffAction;
import com.rs.core.file.managers.PlayerFilesManager;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.World;

/**
 * @author FuzzyAvacado
 */
public class EmptyInventoryAction implements StaffAction {

    @Override
    public void handle(Player player, String value) {
        Player target = World.getPlayerByDisplayName(value);
        if (target == null) {
            target = PlayerFilesManager.loadPlayer(Utils
                    .formatPlayerNameForProtocol(value));
            if (target != null) {
                target.setUsername(Utils
                        .formatPlayerNameForProtocol(value));
            }
        }
        assert target != null;
        target.getInventory().reset();
        player.sendMessage("You reset the inventory of " + target.getDisplayName());
    }

    @Override
    public PlayerRank getMinimumRights() {
        return PlayerRank.ADMIN;
    }
}
