package com.rs.content.commands.impl.owner;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.customskills.sailing.BattleShipController;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;

/**
 * @author John (FuzzyAvacado) on 3/13/2016.
 */
@CommandInfo(name = "battleship", rank = PlayerRank.OWNER)
public class BattleShipControllerCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.getControllerManager().startController(BattleShipController.class);
    }
}
