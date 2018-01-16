package com.rs.world.npc.others;

import com.rs.world.Entity;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public class PestPortal extends NPC {

    public PestPortal(final int id, final WorldTile tile) {
        super(id, tile, -1, true, true);
        setCantFollowUnderCombat(true);
    }

    @Override
    public void processNPC() {
        if (isDead())
            return;
        cancelFaceEntityNoCheck();
    }

    @Override
    public void sendDeath(final Entity killer) {
        resetWalkSteps();
        getCombat().removeTarget();
        super.sendDeath(killer);
    }
}