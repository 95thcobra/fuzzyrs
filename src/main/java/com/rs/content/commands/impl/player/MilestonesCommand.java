package com.rs.content.commands.impl.player;

import com.rs.server.Server;
import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "milestones")
public class MilestonesCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.getInterfaceManager().sendInterface(275);
        for (int i = 0; i < 60; i++) {
            player.getPackets().sendIComponentText(275, i, " ");
        }
        player.getPackets().sendIComponentText(275, 1,
                Server.getInstance().getSettingsManager().getSettings().getServerName() + " Milestones");
        player.getPackets().sendIComponentText(275, 15,
                "Requirements for the Completionist Cape:");
        player.getPackets().sendIComponentText(275, 16, "Monsters");
        player.getPackets()
                .sendIComponentText(275, 18,
                        "You must kill 100 bosses in the Dominion Tower. (trimmed only)");
        //TODO fix godwars objective
        /*player.getPackets().sendIComponentText(
                275,
                20,
                "You must kill General Graardor " + player.bandos
                        + "/20 times.");
        player.getPackets().sendIComponentText(
                275,
                21,
                "You must kill Kree'arra " + player.armadyl
                        + "/10 times.");*/
        player.getPackets().sendIComponentText(275, 23, " ");
        player.getPackets().sendIComponentText(275, 24,
                "Minigames and Quests");
        player.getPackets().sendIComponentText(275, 25, " ");
        player.getPackets()
                .sendIComponentText(275, 26,
                        "You must kill the Queen Black Dragon once. (trimmed only)");
        player.getPackets().sendIComponentText(275, 29,
                "You must complete the Child's Quest. (trimmed only)");
        player.getPackets().sendIComponentText(275, 30,
                "You must complete Cook's Assistant.");
        player.getPackets().sendIComponentText(275, 31,
                "You must complete Nomad's Requiem. (trimmed only)");
        player.getPackets()
                .sendIComponentText(275, 32,
                        "You must complete the Border Guard's quest. (trimmed only)");
        player.getPackets().sendIComponentText(275, 34, "Skilling");
        player.getPackets().sendIComponentText(275, 35, " ");
        player.getPackets().sendIComponentText(275, 44, " ");
        player.getPackets()
                .sendIComponentText(275, 45, "Miscellaneous");
        player.getPackets().sendIComponentText(275, 46, " ");
        player.getPackets().sendIComponentText(275, 47, "None");
        player.getPackets().sendIComponentText(275, 48, " ");
    }
}
