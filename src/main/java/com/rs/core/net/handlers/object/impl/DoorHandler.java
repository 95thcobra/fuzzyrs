package com.rs.core.net.handlers.object.impl;

import com.rs.core.net.handlers.PacketHandler;
import com.rs.player.Player;
import com.rs.world.World;
import com.rs.world.WorldObject;
import com.sun.istack.internal.NotNull;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class DoorHandler implements PacketHandler {

    @Override
    public boolean process(@NotNull Player player, Object... parameters) {
        WorldObject object = (WorldObject) parameters[0];
        final long timer = parameters.length < 2 ? 60000 : (long) parameters[1];
        if (World.isSpawnedObject(object))
            return false;
        final WorldObject openedDoor = new WorldObject(object.getId(),
                object.getType(), object.getRotation() + 1, object.getX(),
                object.getY(), object.getPlane());
        if (object.getRotation() == 0) {
            openedDoor.moveLocation(-1, 0, 0);
        } else if (object.getRotation() == 1) {
            openedDoor.moveLocation(0, 1, 0);
        } else if (object.getRotation() == 2) {
            openedDoor.moveLocation(1, 0, 0);
        } else if (object.getRotation() == 3) {
            openedDoor.moveLocation(0, -1, 0);
        }
        if (World.removeTemporaryObject(object, timer, true)) {
            player.faceObject(openedDoor);
            World.spawnTemporaryObject(openedDoor, timer, true);
            return true;
        }
        return false;
    }
}
