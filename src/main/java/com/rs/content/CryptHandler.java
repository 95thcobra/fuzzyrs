package com.rs.content;

import com.rs.player.Player;
import com.rs.world.WorldTile;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class CryptHandler {

    public static boolean handleCrypt(Player player, int objectId, int objectX, int objectY) {
        for (Crypt c : Crypt.values()) {
            if (c.objectId == objectId && c.xLoc == objectX && c.yLoc == objectY) {
                player.setNextWorldTile(c.worldTile);
                player.getPackets().sendGameMessage("You've broken into a crypt.");
                return true;
            }
        }
        return false;
    }

    public enum Crypt {

        AHRIM_CRYPT(66116, 3567, 3288, new WorldTile(3557, 9703, 3)),
        TORAG_CRYPT(66116, 3554, 3282, new WorldTile(3568, 9683, 3)),
        DHAROK_CRYPT(66116, 3575, 3298, new WorldTile(3556, 9718, 3)),
        VERAC_CRYPT(66115, 3557, 3298, new WorldTile(3578, 9706, 3)),
        KARIL_CRYPT(66115, 3564, 3277, new WorldTile(3546, 9684, 3)),
        GUTHAN_CRYPT(66115, 3576, 3281, new WorldTile(3534, 9704, 3));

        private final int objectId, xLoc, yLoc;
        private final WorldTile worldTile;

        Crypt(int objectId, int xLoc, int yLoc, WorldTile worldTile) {
            this.objectId = objectId;
            this.xLoc = xLoc;
            this.yLoc = yLoc;
            this.worldTile = worldTile;
        }

    }
}
