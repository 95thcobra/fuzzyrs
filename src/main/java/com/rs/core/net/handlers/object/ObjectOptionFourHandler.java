package com.rs.core.net.handlers.object;

import com.rs.Server;
import com.rs.content.actions.skills.mining.MiningBase;
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
public class ObjectOptionFourHandler implements PacketHandler {

    @Override
    public boolean process(@NotNull Player player, Object... parameters) {
        final WorldObject object = (WorldObject) parameters[0];
        final ObjectDefinitions objectDef = object.getDefinitions();
        final int id = object.getId();
        player.setCoordsEvent(new CoordsEvent(object, () -> {
            player.stopAll();
            player.faceObject(object);
            if (!player.getControllerManager().processObjectClick4(object))
                return;
            // living rock Caverns
            if (id == 45076) {
                MiningBase
                        .propect(player,
                                "This rock contains a large concentration of gold.");
            } else if (id == 5999) {
                MiningBase
                        .propect(player,
                                "This rock contains a large concentration of coal.");
            } else {
                switch (objectDef.name.toLowerCase()) {
                    default:
                        player.getPackets().sendGameMessage(
                                "Nothing interesting happens.");
                        break;
                }
            }
            if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
                Logger.info("ObjectHandler", "clicked 4 at object id : " + id
                        + ", " + object.getX() + ", " + object.getY()
                        + ", " + object.getPlane() + ", ");
            }
        }, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
        return false;
    }
}
