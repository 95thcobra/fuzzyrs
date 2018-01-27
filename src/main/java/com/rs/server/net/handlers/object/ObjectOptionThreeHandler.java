package com.rs.server.net.handlers.object;

import com.rs.server.Server;
import com.rs.core.cache.loaders.ObjectDefinitions;
import com.rs.server.net.handlers.PacketHandler;
import com.rs.server.net.handlers.PacketHandlerManager;
import com.rs.server.net.handlers.object.impl.DoorHandler;
import com.rs.server.net.handlers.object.impl.GateHandler;
import com.rs.server.net.handlers.object.impl.LadderHandler;
import com.rs.server.net.handlers.object.impl.StaircaseHandler;
import com.rs.utils.Logger;
import com.rs.player.CoordsEvent;
import com.rs.player.Player;
import com.rs.world.WorldObject;
import com.sun.istack.internal.NotNull;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class ObjectOptionThreeHandler implements PacketHandler {

    @Override
    public boolean process(@NotNull Player player, Object... parameters) {
        final WorldObject object = (WorldObject) parameters[0];
        final ObjectDefinitions objectDef = object.getDefinitions();
        final int id = object.getId();
        player.setCoordsEvent(new CoordsEvent(object, () -> {
            player.stopAll();
            player.faceObject(object);
            if (!player.getControllerManager().processObjectClick3(object))
                return;
            switch (objectDef.name.toLowerCase()) {
                case "bank":
                case "bank chest":
                case "bank booth":
                case "counter":
                    if (objectDef.containsOption(1, "Bank"))
                        player.getGeManager().openCollectionBox();
                    break;
                case "gate":
                case "metal door":
                    if (object.getType() == 0
                            && objectDef.containsOption(2, "Open")) {
                        PacketHandlerManager.get(GateHandler.class).process(player, object);
                    }
                    break;
                case "door":
                    if (object.getType() == 0
                            && objectDef.containsOption(2, "Open")) {
                        PacketHandlerManager.get(DoorHandler.class).process(player, object);
                    }
                    break;
                case "ladder":
                    PacketHandlerManager.get(LadderHandler.class).process(player, object, 3);
                    break;
                case "staircase":
                    PacketHandlerManager.get(StaircaseHandler.class).process(player, object, 3);
                    break;
                default:
                    player.getPackets().sendGameMessage(
                            "Nothing interesting happens.");
                    break;
            }
            if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
                Logger.info("ObjectHandler", "cliked 3 at object id : " + id
                        + ", " + object.getX() + ", " + object.getY()
                        + ", " + object.getPlane() + ", ");
            }
        }, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
        return false;
    }
}
