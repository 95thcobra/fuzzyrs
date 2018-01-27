package com.rs.content.staff.actions.impl;

import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffAction;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.World;

/**
 * @author FuzzyAvacado
 */
public class ChangeDisplayName implements StaffAction {

    @Override
    public void handle(Player player, String value) {
        String[] strings = value.split(":");
        String name = "";
        for (int i = 1; i < strings.length; i++) {
            name += strings[i] + ((i == strings.length - 1) ? "" : " ");
        }
        final Player target = World.getPlayerByDisplayName(name);
        final String[] invalids = { "<img", "<img=", "<col", "<col=",
                "<shad", "<shad=", "<str>", "<u>" };
        for (final String s : invalids)
            if (target.getDisplayName().contains(s)) {
                target.setDisplayName(Utils
                        .formatPlayerNameForDisplay(target
                                .getDisplayName().replace(s, "")));
                player.getPackets().sendGameMessage(
                        "You changed their display name.");
                target.getPackets()
                        .sendGameMessage(
                                "An administrator has changed your display name.");
            }
    }

    @Override
    public PlayerRank getMinimumRights() {
        return PlayerRank.ADMIN;
    }
}
