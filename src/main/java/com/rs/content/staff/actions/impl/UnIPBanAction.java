package com.rs.content.staff.actions.impl;

import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffAction;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.server.Server;
import com.rs.server.file.impl.PlayerFileManager;

import java.util.Optional;

/**
 * @author FuzzyAvacado
 */
public class UnIPBanAction implements StaffAction {

    @Override
    public void handle(Player player, String value) {
        Optional<Player> targetOptional = Server.getInstance().getPlayerFileManager().load(Utils
                .formatPlayerNameForProtocol(value));
        if (targetOptional.isPresent()) {
            Player target = targetOptional.get();
            Server.getInstance().getIpBanFileManager().unBan(target);
            Server.getInstance().getPlayerFileManager().save(target);
            if (!Server.getInstance().getIpBanFileManager().contains(player.getLastIP())) {
                player.getPackets()
                        .sendGameMessage(
                                "You unipbanned "
                                        + PlayerFileManager.formatUserNameForFile(value)
                                        + ".", true);
            } else {
                player.getPackets().sendGameMessage(
                        "Something went wrong. Contact a developer.",
                        true);
            }
        }
    }

    @Override
    public PlayerRank getMinimumRights() {
        return PlayerRank.ADMIN;
    }
}
