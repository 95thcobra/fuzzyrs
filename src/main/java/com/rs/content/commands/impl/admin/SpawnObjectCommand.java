package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.content.staff.actions.StaffActionManager;
import com.rs.content.staff.actions.impl.SpawnObjectAction;
import com.rs.player.Player;

/**
 * @author John (FuzzyAvacado) on 12/12/2015.
 */
@CommandInfo(name = "object", rank = PlayerRank.DEVELOPER)
public class SpawnObjectCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        StaffActionManager.getStaffAction(SpawnObjectAction.class).handle(player, cmd[1]);
    }
}
