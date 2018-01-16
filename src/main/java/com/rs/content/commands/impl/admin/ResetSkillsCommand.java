package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "reset", rank = PlayerRank.ADMIN)
public class ResetSkillsCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        if (cmd.length < 2) {
            for (int skill = 0; skill < 25; skill++) {
                player.getSkills().addXp(skill, 0);
            }
            return;
        }
        try {
            player.getSkills().setXp(Integer.valueOf(cmd[1]), 0);
            player.getSkills().set(Integer.valueOf(cmd[1]), 1);
        } catch (final NumberFormatException e) {
            player.getPackets().sendPanelBoxMessage(
                    "Use: ::master skill");
        }
    }
}
