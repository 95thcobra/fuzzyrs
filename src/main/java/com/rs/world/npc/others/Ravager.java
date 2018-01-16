package com.rs.world.npc.others;

import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public class Ravager extends NPC {

    boolean destroyingObject = false;

    public Ravager(final int id, final WorldTile tile,
                   final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea) {
        super(id, tile, -1, false, false);
    }

    @Override
    public void processNPC() {
    }

}
