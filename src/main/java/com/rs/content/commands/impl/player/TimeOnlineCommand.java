package com.rs.content.commands.impl.player;

import com.rs.Server;
import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.core.settings.SettingsManager;
import com.rs.player.Player;
import com.rs.world.ForceTalk;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "timeonline")
public class TimeOnlineCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.getInterfaceManager().sendInterface(1245);
        player.getPackets().sendIComponentText(1245, 330,
                "Time Spent Online");
        player.getPackets().sendIComponentText(1245, 13, " ");
        player.getPackets().sendIComponentText(1245, 14, " ");
        player.getPackets().sendIComponentText(1245, 15,
                "You have been playing for " + player.getTime() + " hours.");
        player.getPackets().sendIComponentText(1245, 16, " ");
        player.getPackets().sendIComponentText(1245, 17, " ");
        player.getPackets().sendIComponentText(1245, 18, " ");
        player.getPackets().sendIComponentText(1245, 19, " ");
        player.getPackets().sendIComponentText(1245, 20, " ");
        player.getPackets().sendIComponentText(1245, 21, " ");
        player.getPackets().sendIComponentText(1245, 22, " ");
        player.getPackets().sendIComponentText(1245, 23, " ");
        player.setNextForceTalk(new ForceTalk("I've spent a total of "
                + player.getTime() + " on " + Server.getInstance().getSettingsManager().getSettings().getServerName()));
    }
}
