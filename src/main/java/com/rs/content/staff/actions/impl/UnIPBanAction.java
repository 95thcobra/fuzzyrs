package com.rs.content.staff.actions.impl;

import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffAction;
import com.rs.core.file.managers.IPBanFileManager;
import com.rs.core.file.managers.PlayerFilesManager;
import com.rs.core.utils.Utils;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
public class UnIPBanAction implements StaffAction {

    @Override
    public void handle(Player player, String value) {
        Player target = PlayerFilesManager.loadPlayer(Utils
                .formatPlayerNameForProtocol(value));
        IPBanFileManager.unban(target);
        PlayerFilesManager.savePlayer(target);
        if (!IPBanFileManager.getList().contains(player.getLastIP())) {
            player.getPackets()
                    .sendGameMessage(
                            "You unipbanned "
                                    + Utils.formatPlayerNameForProtocol(value)
                                    + ".", true);
        } else {
            player.getPackets().sendGameMessage(
                    "Something went wrong. Contact a developer.",
                    true);
        }
    }

    @Override
    public PlayerRank getMinimumRights() {
        return PlayerRank.ADMIN;
    }
}
