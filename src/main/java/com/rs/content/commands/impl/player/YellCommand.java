package com.rs.content.commands.impl.player;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.player.content.YellHandler;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "yell")
public class YellCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        String message = "";
        for (int i = 1; i < cmd.length; i++) {
            message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
        }
        YellHandler.sendYell(player, Utils.fixChatMessage(message));
    }
}
