package com.rs.content.staff.actions.impl;

import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffAction;
import com.rs.server.GameConstants;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.server.Server;

import java.io.File;
import java.util.Optional;

/**
 * @author FuzzyAvacado
 */
public class UnBanAction implements StaffAction {

    @Override
    public void handle(Player player, String value) {
        final File acc = new File(GameConstants.DATA_PATH + "/playersaves/characters/"
                + value.replace(" ", "_") + ".p");
        Player target = null;
        Optional<Player> targetOptional = Server.getInstance().getPlayerFileManager().load(value);
        if (targetOptional.isPresent()) {
            target = targetOptional.get();
            target.setUsername(Utils
                    .formatPlayerNameForProtocol(value));
        }
        assert target != null;
        target.setPermBanned(false);
        target.setBanned(0);
        player.getPackets().sendGameMessage("You've unbanned " + Utils.formatPlayerNameForDisplay(target.getUsername()) + ".");
        Server.getInstance().getPlayerFileManager().save(target);
    }

    @Override
    public PlayerRank getMinimumRights() {
        return PlayerRank.ADMIN;
    }
}
