package com.rs.world.npc.others;

import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public class MasterOfFear extends NPC {

    public MasterOfFear(final int id, final WorldTile tile,
                        final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea,
                        final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
        setName("Master of fear");
    }
}
