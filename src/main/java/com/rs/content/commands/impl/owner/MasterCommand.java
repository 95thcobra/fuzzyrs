package com.rs.content.commands.impl.owner;

import com.rs.content.actions.skills.Skills;
import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.customskills.CustomSkills;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "master", rank = PlayerRank.OWNER)
public class MasterCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        if (cmd.length < 2) {
            for (int skill = 0; skill < 25; skill++) {
                player.getSkills().addXp(skill, Skills.MAXIMUM_EXP);
            }
            for (CustomSkills skill : CustomSkills.values()) {
                player.getCustomSkills().setLevel(skill, 120);
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
