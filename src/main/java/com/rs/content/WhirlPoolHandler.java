package com.rs.content;

import com.rs.player.Player;
import com.rs.server.Server;
import com.rs.world.Animation;
import com.rs.world.ForceMovement;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.task.gametask.GameTask;
import com.rs.task.gametask.GameTaskManager;

import java.util.concurrent.TimeUnit;

/**
 * @author John (FuzzyAvacado) on 1/6/2016.
 */
public class WhirlPoolHandler {

    public static void handleWirlPool(Player player, WorldObject object) {
        if (object.getId() == 67966 && object.getX() == 2510 && object.getY() == 3506 && object.getPlane() == 0) {
            final int speed = 4; //lower makes the sprint and jump faster
            player.lock(11); //You need this unless you want to be able to do funny looking glitches.
            player.addWalkSteps(player.getX(), 3515, 0, false); //makes the player run to the correct spot (real runescape spot is 3516)

            Server.getInstance().getGameTaskManager().scheduleTask(new GameTask(GameTask.ExecutionType.FIXED_RATE, 0, 600, TimeUnit.MILLISECONDS) {
                int step = 0;
                @Override
                public void run() {
                    switch (step) {
                        case 0:
                            player.setNextFaceWorldTile(new WorldTile(player.getX(), player.getY() - 1, 0));
                            break;
                        case 3:
                            player.faceObject(object);
                            player.setNextAnimation(new Animation(6723));
                            break;
                        case 5:
                            final WorldTile toTile = new WorldTile(player.getX(), 3509, 0);
                            player.faceObject(object);
                            player.setNextForceMovement(new ForceMovement(player, 0, toTile, speed, ForceMovement.SOUTH));
                            break;
                        case 10:
                            player.setNextWorldTile(new WorldTile(1763, 5365, 1));
                            player.getPackets().sendGameMessage("You dive into the swirling  maelstrom of the whirlpool.", true);
                            player.getPackets().sendGameMessage("You are swirled beneath the water, the darkness and pressure are overwhelming.", true);
                            player.getPackets().sendGameMessage("Mystical forces guide you into a cavern below the whirlpool.", true);
                            break;
                    }
                    step++;
                }
            }, GameTaskManager.GameTaskType.FAST);
        }
    }
}
