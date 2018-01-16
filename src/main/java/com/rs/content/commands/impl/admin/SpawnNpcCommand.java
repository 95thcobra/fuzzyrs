package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffActionManager;
import com.rs.content.staff.actions.impl.SpawnNpcAction;
import com.rs.player.Player;

/**
 * @author John (FuzzyAvacado) on 12/12/2015.
 */
@CommandInfo(name = "npc", rank = PlayerRank.DEVELOPER)
public class SpawnNpcCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        StaffActionManager.getStaffAction(SpawnNpcAction.class).handle(player, cmd[1]);
    }
}
