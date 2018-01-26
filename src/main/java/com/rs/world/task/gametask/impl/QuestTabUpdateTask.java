package com.rs.world.task.gametask.impl;

import com.rs.server.Server;
import com.rs.content.customskills.CustomSkills;
import com.rs.content.player.points.PlayerPoints;
import com.rs.core.cores.ServerOnlineTime;
import com.rs.player.Player;
import com.rs.world.World;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * @author FuzzyAvacado
 */
@GameTaskType(GameTaskManager.GameTaskType.FAST)
public class QuestTabUpdateTask extends GameTask {

    public static final String LINE_BREAK = "<br>";
    private static final String[] INFORMATION = new String[30];

    public QuestTabUpdateTask() {
        super(GameTask.ExecutionType.FIXED_DELAY, 0, 5, TimeUnit.SECONDS);
    }

    public static void sendQuestTab(Player player) {
        int playercount = World.getPlayers().size();
        int pHours = player.getTime() / 60;
        int pDays = pHours / 24;
        int pMinutes = player.getTime() % 60;
        player.getPackets().sendIComponentText(930, 10, "<col=" + Color.MAGENTA.hashCode() + ">" + Server.getInstance().getSettingsManager().getSettings().getServerName());
        INFORMATION[0] = "Players online:<col=9ECBFF> " + playercount;
        INFORMATION[2] = "Server Online:";
        INFORMATION[3] = "<col=9ECBFF>Days: " + ServerOnlineTime.getDays() + " Hours: " + ServerOnlineTime.getHours() + " Minutes: " + ServerOnlineTime.getMinutes();
        INFORMATION[5] = "Time played:";
        INFORMATION[6] = "<col=9ECBFF>Days: " + pDays + " Hours: " + pHours + " Minutes: " + pMinutes;
        INFORMATION[8] = "Game mode:<col=9ECBFF> None";
        INFORMATION[10] = "Skills:";
        INFORMATION[11] = "<col=9ECBFF>Slayer task: " + (player.getTask() == null ? "None" : player.getTask().getTaskAmount() + " " + player.getTask().getName().toLowerCase() + "s.");
        INFORMATION[12] = "<col=9ECBFF>Sailing Level: " + player.getCustomSkills().getLevel(CustomSkills.SAILING);
        INFORMATION[14] = "Player points:";
        int counter = 0;
        for (PlayerPoints p : PlayerPoints.values()) {
            if (p == PlayerPoints.RUNESPAN_POINTS) {
                continue;
            }
            INFORMATION[(++counter) + 14] = "<col=76A5DB>" + p.formattedName() + ":<col=9ECBFF> " + player.getPlayerPoints().getPoints(p);
        }
        StringBuilder builder = new StringBuilder();
        for (String s : INFORMATION) {
            if (s == null) {
                builder.append(LINE_BREAK);
                continue;
            }
            builder.append(s).append(LINE_BREAK);
        }
        player.getPackets().sendIComponentText(930, 16, builder.toString());
        player.getPackets().sendIComponentText(550, 18, "Players Online: <col=ff0000>" + World.getPlayers().size());
    }

    @Override
    public void run() {
        for (final Player player : World.getPlayers()) {
            if (player != null) {
                sendQuestTab(player);
            }
        }
    }
}
