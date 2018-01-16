package com.rs.content.commands.impl.admin;

import com.rs.content.actions.skills.Skills;
import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "master", rank = PlayerRank.ADMIN)
public class MasterSkillsCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        if (cmd.length < 2) {
            for (int skill = 0; skill < 25; skill++) {
                player.getSkills().addXp(skill, Skills.getXPForLevel(99));
            }
            return;
        }
        try {
            player.getSkills().addXp(Integer.valueOf(cmd[1]),
                    Skills.MAXIMUM_EXP);
        } catch (final NumberFormatException e) {
            player.getPackets().sendPanelBoxMessage(
                    "Use: ::master skill");
        }
    }
}
