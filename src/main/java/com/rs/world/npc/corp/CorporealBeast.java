package com.rs.world.npc.corp;

import com.rs.entity.Entity;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public class CorporealBeast extends NPC {

    private DarkEnergyCore core;

    public CorporealBeast(final int id, final WorldTile tile,
                          final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea,
                          final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
        setCapDamage(1000);
        setLureDelay(3000);
        setForceTargetDistance(64);
        setForceFollowClose(false);
    }

    public void spawnDarkEnergyCore() {
        if (core != null)
            return;
        core = new DarkEnergyCore(this);
    }

    public void removeDarkEnergyCore() {
        if (core == null)
            return;
        core.finish();
        core = null;
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (isDead())
            return;
        final int maxhp = getMaxHitpoints();
        if (maxhp > getHitpoints() && getPossibleTargets().isEmpty()) {
            setHitpoints(maxhp);
        }
    }

    @Override
    public void sendDeath(final Entity source) {
        super.sendDeath(source);
        if (core != null) {
            core.sendDeath(source);
        }
    }

    @Override
    public double getMagePrayerMultiplier() {
        return 0.6;
    }

}
