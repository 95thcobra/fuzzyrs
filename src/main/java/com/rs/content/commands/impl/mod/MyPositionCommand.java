package com.rs.content.commands.impl.mod;

import com.rs.content.commands.Command;
import com.rs.content.commands.CommandInfo;
import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
@CommandInfo(name = "mypos")
public class MyPositionCommand implements Command {

    @Override
    public void handle(Player player, String[] cmd) {
        player.getPackets().sendGameMessage("Coords: " + player.getX() + ", " + player.getY()
                + ", " + player.getPlane() + ", regionId: "
                + player.getRegionId() + ", rx: "
                + player.getChunkX() + ", ry: "
                + player.getChunkY(), true);
    }
}
