package com.rs.world.npc.dragons;

import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public class KingBlackDragon extends NPC {

    public KingBlackDragon(final int id, final WorldTile tile,
                           final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea,
                           final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
        setLureDelay(0);
    }

    public static boolean atKBD(final WorldTile tile) {
        return (tile.getX() >= 2250 && tile.getX() <= 2292)
                && (tile.getY() >= 4675 && tile.getY() <= 4710);
    }

}
