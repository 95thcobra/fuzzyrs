package com.rs.world.npc.others;

import com.rs.world.Hit;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public class Pker extends NPC {

    public Pker(final int id, final WorldTile tile, final int mapAreaNameHash,
                final boolean canBeAttackFromOutOfArea, final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
        setName("Isaiah");
    }

    @Override
    public double getMagePrayerMultiplier() {
        return 0.7;
    }

    @Override
    public double getRangePrayerMultiplier() {
        return 0.7;
    }

    @Override
    public double getMeleePrayerMultiplier() {
        return 0.7;
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        super.handleIngoingHit(hit);
        if (hit.getLook() != Hit.HitLook.MELEE_DAMAGE
                && hit.getLook() != Hit.HitLook.RANGE_DAMAGE
                && hit.getLook() != Hit.HitLook.MAGIC_DAMAGE)
            return;
        if (hit.getSource() != null) {
            final int recoil = 0;
            if (recoil > 0) {
                hit.getSource().applyHit(
                        new Hit(this, recoil, Hit.HitLook.REFLECTED_DAMAGE));
            }
        }
    }

}