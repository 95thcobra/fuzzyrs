package com.rs.content.commands.impl.mod;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffActionManager;
import com.rs.content.staff.actions.impl.BanAction;
import com.rs.content.staff.actions.impl.UnBanAction;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "unban", rank = PlayerRank.MOD)
public class UnBanCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        String name = "";
        for (int i = 1; i < cmd.length; i++) {
            name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
        }
        StaffActionManager.getStaffAction(UnBanAction.class).handle(player, name);
    }
}
