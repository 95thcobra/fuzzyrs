package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.content.staff.StaffPanelDialogue;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "cp", rank = PlayerRank.ADMIN)
public class OpenAdminCPCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.getDialogueManager().startDialogue(StaffPanelDialogue.class);
    }
}
