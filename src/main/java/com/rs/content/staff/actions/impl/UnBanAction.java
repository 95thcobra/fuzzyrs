package com.rs.content.staff.actions.impl;

import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffAction;
import com.rs.core.file.DataFile;
import com.rs.core.file.managers.PlayerFilesManager;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Utils;
import com.rs.player.Player;

import java.io.File;

/**
 * @author FuzzyAvacado
 */
public class UnBanAction implements StaffAction {

    @Override
    public void handle(Player player, String value) {
        final File acc = new File(GameConstants.DATA_PATH + "/playersaves/characters/"
                + value.replace(" ", "_") + ".p");
        Player target = null;
        target = new DataFile<Player>(acc).fromSerialUnchecked();
        assert target != null;
        target.setPermBanned(false);
        target.setBanned(0);
        player.getPackets().sendGameMessage("You've unbanned " + Utils.formatPlayerNameForDisplay(target.getUsername()) + ".");
        PlayerFilesManager.savePlayer(target);
    }

    @Override
    public PlayerRank getMinimumRights() {
        return PlayerRank.ADMIN;
    }
}
