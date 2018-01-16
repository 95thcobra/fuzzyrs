package com.rs.core.net.handlers.object;

import com.rs.content.actions.skills.firemaking.Bonfire;
import com.rs.core.cache.loaders.ObjectDefinitions;
import com.rs.core.net.handlers.PacketHandler;
import com.rs.core.settings.SettingsManager;
import com.rs.core.utils.Logger;
import com.rs.player.CoordsEvent;
import com.rs.player.Player;
import com.rs.world.WorldObject;
import com.sun.istack.internal.NotNull;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class ObjectOptionFiveHandler implements PacketHandler {

    @Override
    public boolean process(@NotNull Player player, Object... parameters) {
        final WorldObject object = (WorldObject) parameters[0];
        final ObjectDefinitions objectDef = object.getDefinitions();
        final int id = object.getId();
        player.setCoordsEvent(new CoordsEvent(object, () -> {
            player.stopAll();
            player.faceObject(object);
            if (!player.getControllerManager().processObjectClick5(object))
                return;
            switch (objectDef.name.toLowerCase()) {
                case "fire":
                    if (objectDef.containsOption(4, "Add-logs")) {
                        Bonfire.addLogs(player, object);
                    }
                    break;
                default:
                    player.getPackets().sendGameMessage(
                            "Nothing interesting happens.");
                    break;
            }
            if (SettingsManager.getSettings().DEBUG) {
                Logger.info("ObjectHandler", "cliked 5 at object id : " + id
                        + ", " + object.getX() + ", " + object.getY()
                        + ", " + object.getPlane() + ", ");
            }
        }, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
        return false;
    }
}
