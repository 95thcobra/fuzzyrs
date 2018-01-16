package com.rs.content.staff.actions.impl;

import com.rs.content.economy.shops.ShopsManager;
import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffAction;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
public class InitializeShops implements StaffAction {

    @Override
    public void handle(Player player, String value) {
        ShopsManager.init();
    }

    @Override
    public PlayerRank getMinimumRights() {
        return PlayerRank.ADMIN;
    }
}
