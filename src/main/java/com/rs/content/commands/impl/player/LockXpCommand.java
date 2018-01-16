package com.rs.content.commands.impl.player;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "lockxp")
public class LockXpCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.setXpLocked(!player.isXpLocked());
        player.getPackets().sendGameMessage("You have " + (player.isXpLocked() ? "UNLOCKED" : "LOCKED") + " your xp.");
    }
}
