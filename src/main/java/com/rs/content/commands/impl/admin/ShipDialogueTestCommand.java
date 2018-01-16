package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;

/**
 * @author John (FuzzyAvacado) on 12/17/2015.
 */
@CommandInfo(name = "shiptest", rank = PlayerRank.DEVELOPER)
public class ShipDialogueTestCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
    }
}
