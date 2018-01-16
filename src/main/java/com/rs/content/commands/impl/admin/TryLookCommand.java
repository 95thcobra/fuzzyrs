package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "trylook", rank = PlayerRank.ADMIN)
public class TryLookCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        final int look = Integer.parseInt(cmd[1]);
        WorldTasksManager.schedule(new WorldTask() {
            int i = 269;// 200

            @Override
            public void run() {
                if (player.hasFinished()) {
                    stop();
                }
                player.getAppearance().setLook(look, i);
                player.getAppearance().generateAppearenceData();
                player.getPackets().sendGameMessage("Look " + i + ".");
                i++;
            }
        }, 0, 1);
    }
}
