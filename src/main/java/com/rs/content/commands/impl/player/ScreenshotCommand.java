package com.rs.content.commands.impl.player;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "screenshot")
public class ScreenshotCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.getPackets().sendGameMessage(":screenshot:");
    }
}
