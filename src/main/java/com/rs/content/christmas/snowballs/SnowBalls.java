package com.rs.content.christmas.snowballs;

import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.server.Server;
import com.rs.world.Animation;
import com.rs.world.World;
import com.rs.world.WorldObject;
import com.rs.task.gametask.GameTask;

import java.util.concurrent.TimeUnit;

/**
 * @author FuzzyAvacado
 */
public class SnowBalls {

    public static void handleSnowBallPacket(Player player, Player target) {
        if (target == null || target.isDead() || target.hasFinished()
                || !player.getMapRegionsIds().contains(target.getRegionId()))
            return;
        if (player.getLockDelay() > Utils.currentTimeMillis())
            return;
        if (player.getInterfaceManager().containsScreenInter()) {
            player.getPackets().sendGameMessage("The other player is busy.");
            return;
        }
        if (!target.withinDistance(player, 14)) {
            player.getPackets().sendGameMessage(
                    "Unable to find target " + target.getDisplayName());
            return;
        }
        if (player.getAttackedByDelay() + 10000 > Utils.currentTimeMillis()) {
            player.getPackets()
                    .sendGameMessage(
                            "<col=B00000>You can't do this until 10 seconds after the end of combat.");
            return;
        }
        if (player.getEquipment().getWeaponId() == 11951) {
            player.getEquipment().deleteItem(11951, 1);
        } else if (player.getInventory().containsItem(11951, 1)) {
            player.getInventory().deleteItem(11951, 1);
        } else {
            player.getPackets().sendGameMessage("You need a snowball to throw at them silly!");
            return;
        }
        player.faceEntity(target);
        player.setNextAnimation(new Animation(7530));
        World.sendProjectile(player, player, target, 1281, 21, 21, 90, 65, 50, 0);
        Server.getInstance().getGameTaskManager().scheduleTask(new ThrowSnowballTask(target, 3, GameTask.ExecutionType.FIXED_DELAY, 0, 600, TimeUnit.MILLISECONDS));
    }

    public static void handleObjectClick(Player player, WorldObject object) {
        player.setNextAnimation(new Animation(1026));
        int i = player.getInventory().getFreeSlots();
        player.getInventory().addItem(11951, i);
        player.getPackets().sendGameMessage("You carefully form " + i + " snowballs and stow them away.");
        World.removeTemporaryObject(object, 60000, false);
        player.sendDefaultPlayersOptions();
    }

    public static void handleItemClick(Player player) {
        player.lock(3);
        World.spawnObject(new WorldObject(28297, 10, 0,player.getX(), player.getY(), player.getPlane()), true);
        player.setNextAnimation(new Animation(1745));
        player.getInterfaceManager().sendInterface(659);
        int i = player.getInventory().getFreeSlots();
        player.getInventory().addItem(11951, i);
        player.sendMessage("The snow globe fills your inventory with snow!");
        player.sendDefaultPlayersOptions();
    }
}
