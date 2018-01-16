package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "quake", rank = PlayerRank.ADMIN)
public class SendEarthquakeCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.getPackets().sendCameraShake(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]), Integer.valueOf(cmd[3]),
                Integer.valueOf(cmd[4]), Integer.valueOf(cmd[5]));
    }
}
