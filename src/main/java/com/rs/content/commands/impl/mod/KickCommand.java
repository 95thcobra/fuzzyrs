package com.rs.content.commands.impl.mod;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffActionManager;
import com.rs.content.staff.actions.impl.KickAction;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "kick", rank = PlayerRank.MOD)
public class KickCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        String name = "";
        for (int i = 1; i < cmd.length; i++) {
            name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
        }
        StaffActionManager.getStaffAction(KickAction.class).handle(player, name);
    }
}
