package com.rs.content.staff.actions.impl;

import com.rs.Server;
import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffAction;
import com.rs.core.settings.SettingsManager;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
public class InitializeServerSettings implements StaffAction {

    @Override
    public void handle(Player player, String value) {
        Server.getInstance().getSettingsManager().init();
    }

    @Override
    public PlayerRank getMinimumRights() {
        return PlayerRank.ADMIN;
    }
}
