package com.rs.content.staff.actions.impl;

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
        SettingsManager.init();
    }

    @Override
    public PlayerRank getMinimumRights() {
        return PlayerRank.ADMIN;
    }
}
