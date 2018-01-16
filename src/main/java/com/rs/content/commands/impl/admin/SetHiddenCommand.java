package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "hide", rank = PlayerRank.ADMIN)
public class SetHiddenCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.getAppearance().switchHidden();
        player.getPackets().sendGameMessage("Am i hidden? " + player.getAppearance().isHidden());
    }
}
