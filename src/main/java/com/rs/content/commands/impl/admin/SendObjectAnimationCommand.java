package com.rs.content.commands.impl.admin;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.content.player.PlayerRank;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.World;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "objectanim", rank = PlayerRank.ADMIN)
public class SendObjectAnimationCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        final WorldObject object = cmd.length == 4 ? World
                .getObject(new WorldTile(Integer.parseInt(cmd[1]),
                        Integer.parseInt(cmd[2]), player.getPlane()))
                : World.getObject(
                new WorldTile(Integer.parseInt(cmd[1]), Integer
                        .parseInt(cmd[2]), player.getPlane()),
                Integer.parseInt(cmd[3]));
        if (object == null) {
            player.getPackets().sendPanelBoxMessage(
                    "No object was found.");
            return;
        }
        player.getPackets().sendObjectAnimation(
                object,
                new Animation(Integer.parseInt(cmd[cmd.length == 4 ? 3
                        : 4])));
    }
}
