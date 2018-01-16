package com.rs.content.commands.impl.owner;

import com.rs.content.actions.skills.Skills;
import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;
import com.rs.world.World;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "setlevelother", rank = PlayerRank.OWNER)
public class SetLevelOtherCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
        Player other = World.getPlayers().get(World.getIdFromName(username));
        if (other == null) {
            player.getPackets().sendGameMessage(
                    "There is no such player as " + username + ".");
            return;
        }
        final int skill = Integer.parseInt(cmd[2]);
        int level = Integer.parseInt(cmd[3]);
        other.getSkills().set(Integer.parseInt(cmd[2]),
                Integer.parseInt(cmd[3]));
        other.getSkills().set(skill, level);
        other.getSkills().setXp(skill, Skills.getXPForLevel(level));
        other.getPackets().sendGameMessage(
                "One of your skills:  "
                        + other.getSkills().getLevel(skill)
                        + " has been set to " + level + " from "
                        + player.getDisplayName() + ".");
        player.getPackets().sendGameMessage(
                "You have set the skill:  "
                        + other.getSkills().getLevel(skill) + " to "
                        + level + " for " + other.getDisplayName()
                        + ".");
    }
}
