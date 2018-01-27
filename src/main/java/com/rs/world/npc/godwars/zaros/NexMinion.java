package com.rs.world.npc.godwars.zaros;

import com.rs.content.minigames.ZarosGodwars;
import com.rs.entity.Entity;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public class NexMinion extends NPC {

    private boolean hasNoBarrier;

    public NexMinion(final int id, final WorldTile tile,
                     final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea,
                     final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
        setCantFollowUnderCombat(true);
        setCapDamage(0);
    }

    public void breakBarrier() {
        setCapDamage(-1);
        hasNoBarrier = true;
    }

    @Override
    public void processNPC() {
        if (isDead() || !hasNoBarrier)
            return;
        if (!getCombat().process()) {
            checkAgressivity();
        }
    }

    @Override
    public void sendDeath(final Entity source) {
        super.sendDeath(source);
        ZarosGodwars.moveNextStage();
    }

}
