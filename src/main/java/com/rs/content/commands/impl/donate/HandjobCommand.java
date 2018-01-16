package com.rs.content.commands.impl.donate;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.ForceTalk;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "handjob", donateRank = PlayerRank.DonateRank.VIP)
public class HandjobCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.setNextAnimation(new Animation(2424));
        player.setNextForceTalk(new ForceTalk("Let me give you a nice handjob... om nom nom"));
    }
}
