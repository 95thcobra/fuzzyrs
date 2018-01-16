package com.rs.content.commands.impl.donate;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "yellcolor", donateRank = PlayerRank.DonateRank.EXTREME_DONATOR)
public class ChangeYellColorCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.getPackets().sendRunScript(109, "Please enter the yell color.");
        player.getTemporaryAttributtes().put("yellcolor", Boolean.TRUE);
    }
}
