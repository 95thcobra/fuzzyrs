package com.rs.content.staff;

import com.rs.content.staff.actions.StaffAction;
import com.rs.content.staff.actions.StaffActionManager;
import com.rs.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FuzzyAvacado
 */
public class StaffPanelHandler {

    private static final Map<Player, StaffAction> usingOptions = new HashMap<>();

    public static void handle(Player player, String value) {
        StaffAction staffAction = usingOptions.remove(player);
        if (staffAction == null)
            return;
        if (!player.getRank().isMinimumRank(staffAction.getMinimumRights())) {
            player.sendMessage("You do not have sufficient rights to use that function.");
        } else {
            staffAction.handle(player, value);
        }
    }

    public static void put(Player player, Class<? extends StaffAction> k) {
        StaffAction staffAction = StaffActionManager.getStaffAction(k);
        usingOptions.put(player, staffAction);
    }

}
