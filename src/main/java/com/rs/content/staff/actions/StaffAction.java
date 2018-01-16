package com.rs.content.staff.actions;

import com.rs.content.player.PlayerRank;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
public interface StaffAction {

    void handle(Player player, String value);

    PlayerRank getMinimumRights();
}
