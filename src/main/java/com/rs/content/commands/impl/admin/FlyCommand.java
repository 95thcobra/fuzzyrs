package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "fly", rank = PlayerRank.ADMIN)
public class FlyCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.getAppearance().setRenderEmote(1027);
    }
}
