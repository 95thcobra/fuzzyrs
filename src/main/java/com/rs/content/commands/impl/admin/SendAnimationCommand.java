package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;
import com.rs.world.Animation;

/**
 * @author John (FuzzyAvacado) on 12/12/2015.
 */
@CommandInfo(name = "anim", rank = PlayerRank.ADMIN)
public class SendAnimationCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.setNextAnimation(new Animation(Integer.parseInt(cmd[1])));
    }
}
