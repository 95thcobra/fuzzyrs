package com.rs.core.net.handlers.object.impl;

import com.rs.core.net.handlers.PacketHandler;
import com.rs.player.Player;
import com.rs.world.World;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.sun.istack.internal.NotNull;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class GateHandler implements PacketHandler {

    @Override
    public boolean process(@NotNull Player player, Object... parameters) {
        WorldObject object = (WorldObject) parameters[0];
        if (World.isSpawnedObject(object))
            return false;
        if (object.getRotation() == 0) {

            boolean south = true;
            WorldObject otherDoor = World.getObject(new WorldTile(
                            object.getX(), object.getY() + 1, object.getPlane()),
                    object.getType());
            if (otherDoor == null
                    || otherDoor.getRotation() != object.getRotation()
                    || otherDoor.getType() != object.getType()
                    || !otherDoor.getDefinitions().name.equalsIgnoreCase(object
                    .getDefinitions().name)) {
                otherDoor = World.getObject(
                        new WorldTile(object.getX(), object.getY() - 1, object
                                .getPlane()), object.getType());
                if (otherDoor == null
                        || otherDoor.getRotation() != object.getRotation()
                        || otherDoor.getType() != object.getType()
                        || !otherDoor.getDefinitions().name
                        .equalsIgnoreCase(object.getDefinitions().name))
                    return false;
                south = false;
            }
            final WorldObject openedDoor1 = new WorldObject(object.getId(),
                    object.getType(), object.getRotation() + 1, object.getX(),
                    object.getY(), object.getPlane());
            final WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
                    otherDoor.getType(), otherDoor.getRotation() + 1,
                    otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
            if (south) {
                openedDoor1.moveLocation(-1, 0, 0);
                openedDoor1.setRotation(3);
                openedDoor2.moveLocation(-1, 0, 0);
            } else {
                openedDoor1.moveLocation(-1, 0, 0);
                openedDoor2.moveLocation(-1, 0, 0);
                openedDoor2.setRotation(3);
            }

            if (World.removeTemporaryObject(object, 60000, true)
                    && World.removeTemporaryObject(otherDoor, 60000, true)) {
                player.faceObject(openedDoor1);
                World.spawnTemporaryObject(openedDoor1, 60000, true);
                World.spawnTemporaryObject(openedDoor2, 60000, true);
                return true;
            }
        } else if (object.getRotation() == 2) {

            boolean south = true;
            WorldObject otherDoor = World.getObject(new WorldTile(
                            object.getX(), object.getY() + 1, object.getPlane()),
                    object.getType());
            if (otherDoor == null
                    || otherDoor.getRotation() != object.getRotation()
                    || otherDoor.getType() != object.getType()
                    || !otherDoor.getDefinitions().name.equalsIgnoreCase(object
                    .getDefinitions().name)) {
                otherDoor = World.getObject(
                        new WorldTile(object.getX(), object.getY() - 1, object
                                .getPlane()), object.getType());
                if (otherDoor == null
                        || otherDoor.getRotation() != object.getRotation()
                        || otherDoor.getType() != object.getType()
                        || !otherDoor.getDefinitions().name
                        .equalsIgnoreCase(object.getDefinitions().name))
                    return false;
                south = false;
            }
            final WorldObject openedDoor1 = new WorldObject(object.getId(),
                    object.getType(), object.getRotation() + 1, object.getX(),
                    object.getY(), object.getPlane());
            final WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
                    otherDoor.getType(), otherDoor.getRotation() + 1,
                    otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
            if (south) {
                openedDoor1.moveLocation(1, 0, 0);
                openedDoor2.setRotation(1);
                openedDoor2.moveLocation(1, 0, 0);
            } else {
                openedDoor1.moveLocation(1, 0, 0);
                openedDoor1.setRotation(1);
                openedDoor2.moveLocation(1, 0, 0);
            }
            if (World.removeTemporaryObject(object, 60000, true)
                    && World.removeTemporaryObject(otherDoor, 60000, true)) {
                player.faceObject(openedDoor1);
                World.spawnTemporaryObject(openedDoor1, 60000, true);
                World.spawnTemporaryObject(openedDoor2, 60000, true);
                return true;
            }
        } else if (object.getRotation() == 3) {

            boolean right = true;
            WorldObject otherDoor = World.getObject(new WorldTile(
                            object.getX() - 1, object.getY(), object.getPlane()),
                    object.getType());
            if (otherDoor == null
                    || otherDoor.getRotation() != object.getRotation()
                    || otherDoor.getType() != object.getType()
                    || !otherDoor.getDefinitions().name.equalsIgnoreCase(object
                    .getDefinitions().name)) {
                otherDoor = World.getObject(new WorldTile(object.getX() + 1,
                        object.getY(), object.getPlane()), object.getType());
                if (otherDoor == null
                        || otherDoor.getRotation() != object.getRotation()
                        || otherDoor.getType() != object.getType()
                        || !otherDoor.getDefinitions().name
                        .equalsIgnoreCase(object.getDefinitions().name))
                    return false;
                right = false;
            }
            final WorldObject openedDoor1 = new WorldObject(object.getId(),
                    object.getType(), object.getRotation() + 1, object.getX(),
                    object.getY(), object.getPlane());
            final WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
                    otherDoor.getType(), otherDoor.getRotation() + 1,
                    otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
            if (right) {
                openedDoor1.moveLocation(0, -1, 0);
                openedDoor2.setRotation(0);
                openedDoor1.setRotation(2);
                openedDoor2.moveLocation(0, -1, 0);
            } else {
                openedDoor1.moveLocation(0, -1, 0);
                openedDoor1.setRotation(0);
                openedDoor2.setRotation(2);
                openedDoor2.moveLocation(0, -1, 0);
            }
            if (World.removeTemporaryObject(object, 60000, true)
                    && World.removeTemporaryObject(otherDoor, 60000, true)) {
                player.faceObject(openedDoor1);
                World.spawnTemporaryObject(openedDoor1, 60000, true);
                World.spawnTemporaryObject(openedDoor2, 60000, true);
                return true;
            }
        } else if (object.getRotation() == 1) {

            boolean right = true;
            WorldObject otherDoor = World.getObject(new WorldTile(
                            object.getX() - 1, object.getY(), object.getPlane()),
                    object.getType());
            if (otherDoor == null
                    || otherDoor.getRotation() != object.getRotation()
                    || otherDoor.getType() != object.getType()
                    || !otherDoor.getDefinitions().name.equalsIgnoreCase(object
                    .getDefinitions().name)) {
                otherDoor = World.getObject(new WorldTile(object.getX() + 1,
                        object.getY(), object.getPlane()), object.getType());
                if (otherDoor == null
                        || otherDoor.getRotation() != object.getRotation()
                        || otherDoor.getType() != object.getType()
                        || !otherDoor.getDefinitions().name
                        .equalsIgnoreCase(object.getDefinitions().name))
                    return false;
                right = false;
            }
            final WorldObject openedDoor1 = new WorldObject(object.getId(),
                    object.getType(), object.getRotation() + 1, object.getX(),
                    object.getY(), object.getPlane());
            final WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
                    otherDoor.getType(), otherDoor.getRotation() + 1,
                    otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
            if (right) {
                openedDoor1.moveLocation(0, 1, 0);
                openedDoor1.setRotation(0);
                openedDoor2.moveLocation(0, 1, 0);
            } else {
                openedDoor1.moveLocation(0, 1, 0);
                openedDoor2.setRotation(0);
                openedDoor2.moveLocation(0, 1, 0);
            }
            if (World.removeTemporaryObject(object, 60000, true)
                    && World.removeTemporaryObject(otherDoor, 60000, true)) {
                player.faceObject(openedDoor1);
                World.spawnTemporaryObject(openedDoor1, 60000, true);
                World.spawnTemporaryObject(openedDoor2, 60000, true);
                return true;
            }
        }
        return false;
    }
}
