package com.rs.content.staff.actions.impl;

import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffAction;
import com.rs.player.Player;
import com.rs.server.Server;
import com.rs.server.file.impl.PlayerFileManager;
import com.rs.world.World;

import java.util.Optional;

/**
 * @author FuzzyAvacado
 */
public class CheckBankAction implements StaffAction {

    @Override
    public void handle(Player player, String value) {
        Player target = World.getPlayerByDisplayName(value);
        if (target == null) {
            Optional<Player> targetOptional = Server.getInstance().getPlayerFileManager().load(value);
            if (targetOptional.isPresent()) {
                target = targetOptional.get();
                target.setUsername(PlayerFileManager.formatUserNameForFile(value));
            }
        }
        player.getBank().openPlayerBank(target);
    }

    @Override
    public PlayerRank getMinimumRights() {
        return PlayerRank.MOD;
    }
}
