package com.rs.core.net.handlers.object;

import com.rs.content.player.PlayerRank;
import com.rs.core.cache.loaders.ObjectDefinitions;
import com.rs.core.net.handlers.PacketHandlerManager;
import com.rs.core.net.io.InputStream;
import com.rs.core.settings.SettingsManager;
import com.rs.core.utils.Logger;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.World;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;

public final class ObjectHandler {

    private ObjectHandler() {

    }

    public static void handleOption(final Player player, final InputStream stream, final int option) {
        if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
            return;
        final long currentTime = Utils.currentTimeMillis();
        if (player.getLockDelay() >= currentTime || player.getEmotesManager().getNextEmoteEnd() >= currentTime)
            return;
        final boolean forceRun = stream.readUnsignedByte128() == 1;
        final int id = stream.readIntLE();
        int x = stream.readUnsignedShortLE();
        int y = stream.readUnsignedShortLE128();
        int rotation = 0;
        if (player.isAtDynamicRegion()) {
            rotation = World.getRotation(player.getPlane(), x, y);
            if (rotation == 1) {
                final ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(id);
                y += defs.getSizeY() - 1;
            } else if (rotation == 2) {
                final ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(id);
                x += defs.getSizeY() - 1;
            }
        }
        final WorldTile tile = new WorldTile(x, y, player.getPlane());
        final int regionId = tile.getRegionId();
        if (!player.getMapRegionsIds().contains(regionId))
            return;
        WorldObject mapObject = World.getRegion(regionId).getObject(id, tile);
        if (mapObject == null || mapObject.getId() != id)
            return;
        if (player.isAtDynamicRegion() && World.getRotation(player.getPlane(), x, y) != 0) { // temp
            // fix
            final ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(id);
            if (defs.getSizeX() > 1 || defs.getSizeY() > 1) {
                for (int xs = 0; xs < defs.getSizeX() + 1 && (mapObject == null || mapObject.getId() != id); xs++) {
                    for (int ys = 0; ys < defs.getSizeY() + 1 && (mapObject == null || mapObject.getId() != id); ys++) {
                        tile.setLocation(x + xs, y + ys, tile.getPlane());
                        mapObject = World.getRegion(regionId).getObject(id, tile);
                    }
                }
            }
            if (mapObject == null || mapObject.getId() != id)
                return;
        }
        final WorldObject object = !player.isAtDynamicRegion() ? mapObject : new WorldObject(id, mapObject.getType(), (mapObject.getRotation() + rotation % 4), x, y, player.getPlane());
        player.stopAll(false);
        if (forceRun) {
            player.setRun(forceRun);
        }
        switch (option) {
            case 1:
                PacketHandlerManager.get(ObjectOptionOneHandler.class).process(player, object);
                break;
            case 2:
                PacketHandlerManager.get(ObjectOptionTwoHandler.class).process(player, object);
                break;
            case 3:
                PacketHandlerManager.get(ObjectOptionThreeHandler.class).process(player, object);
                break;
            case 4:
                PacketHandlerManager.get(ObjectOptionFourHandler.class).process(player, object);
                break;
            case 5:
                PacketHandlerManager.get(ObjectOptionFiveHandler.class).process(player, object);
                break;
            case -1:
                handleOptionExamine(player, object);
                break;
        }
    }

    public static void handleOptionExamine(final Player player, final WorldObject object) {
        if (player.getUsername().equalsIgnoreCase("tyler")) {
            final int offsetX = object.getX() - player.getX();
            final int offsetY = object.getY() - player.getY();
            System.out.println("Offsets" + offsetX + " , " + offsetY);
        }
        if (player.getRank().isMinimumRank(PlayerRank.MOD)) {
            player.getPackets().sendGameMessage(
                    "Object - [id=" + object.getId() + ", loc=[" + object.getX() + ", "
                            + object.getY() + ", " + object.getPlane() + "]].");
        }
        player.getPackets().sendGameMessage(
                "It's an " + object.getDefinitions().name + ".");
        if (SettingsManager.getSettings().DEBUG)
            Logger.info("ObjectHandler", "examined object id : " + object.getId() + ", " + object.getX() + ", " + object.getY() + ", " + object.getPlane() + ", " + object.getType() + ", " + object.getRotation() + ", " + object.getDefinitions().name);
    }

    public static void slashWeb(final Player player, final WorldObject object) {
        if (Utils.getRandom(1) == 0) {
            World.spawnTemporaryObject(new WorldObject(object.getId() + 1,
                    object.getType(), object.getRotation(), object.getX(),
                    object.getY(), object.getPlane()), 60000, true);
            player.getPackets().sendGameMessage("You slash through the web!");
        } else {
            player.getPackets().sendGameMessage(
                    "You fail to cut through the web.");
        }
    }

}
