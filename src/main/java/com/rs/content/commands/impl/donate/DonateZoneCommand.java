package com.rs.content.commands.impl.donate;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Graphics;
import com.rs.world.WorldTile;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "dzone", donateRank = PlayerRank.DonateRank.DONATOR)
public class DonateZoneCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        if (!player.getRank().getDonateRank().isMinimumRank(PlayerRank.DonateRank.DONATOR)) {
            player.getPackets()
                    .sendGameMessage(
                            "<col=ff0000>You need to be a Donator to use this command.");
            return;
        }
        if (!player.canSpawn()) {
            player.getPackets()
                    .sendGameMessage(
                            "<col=ff0000>You can't teleport while you're in this area.</col>");
            return;
        }
        WorldTasksManager.schedule(new WorldTask() {
            int loop;
            @Override
            public void run() {
                if (loop == 0) {
                    player.setNextAnimation(new Animation(17191));
                    player.setNextGraphics(new Graphics(3254));
                } else if (loop == 6) {
                    player.setNextWorldTile(new WorldTile(3223, 1506, 0));
                } else if (loop == 7) {
                    player.setNextAnimation(new Animation(16386));
                    player.setNextGraphics(new Graphics(3019));
                }
                loop++;
            }
        }, 0, 1);
    }
}
