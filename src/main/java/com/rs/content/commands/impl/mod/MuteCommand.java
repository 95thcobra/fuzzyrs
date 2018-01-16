package com.rs.content.commands.impl.mod;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffActionManager;
import com.rs.content.staff.actions.impl.MuteAction;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "mute", rank = PlayerRank.MOD)
public class MuteCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        String name = "";
        for (int i = 1; i < cmd.length; i++) {
            name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
        }
        StaffActionManager.getStaffAction(MuteAction.class).handle(player, name);
    }
}
