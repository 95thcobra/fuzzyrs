package com.rs.content.commands.impl.player;

import com.rs.server.Server;
import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.commands.CommandManager;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;

import java.util.List;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "commands")
public class ViewCommandsCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.getInterfaceManager().sendInterface(275);
        for (int i = 0; i < 150; i++) {
            player.getPackets().sendIComponentText(275, i, " ");
        }
        int counter = 11;
        player.getPackets().sendIComponentText(275, 1, Server.getInstance().getSettingsManager().getSettings().getServerName() + " Commands");
        player.getPackets().sendIComponentText(275, 10, "Commands");
        List<String> commandNames;
        for (int i = player.getRank().ordinal(); i >= 0; i--) {
            commandNames = Server.getInstance().getCommandManager().listCommandNames(PlayerRank.values()[i]);
            if (commandNames.isEmpty())
                continue;
            player.getPackets().sendIComponentText(275, counter++, " ");
            player.getPackets().sendIComponentText(275, counter++, "~" + PlayerRank.values()[i].name() + " Commands~");
            player.getPackets().sendIComponentText(275, counter++, " ");
            for (String s : commandNames) {
                player.getPackets().sendIComponentText(275, counter++, s);
            }
        }
    }
}
