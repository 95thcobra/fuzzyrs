package com.rs.content.commands.impl.player;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "switchitemlooks")
public class SwitchItemLooksCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.switchItemsLook();
        player.getPackets().sendGameMessage("You are now playing with " + (player.isOldItemsLook() ? "old" : "new") + " item looks.");
    }
}
