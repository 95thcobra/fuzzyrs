package com.rs.world.npc.others;

import com.rs.world.Hit;
import com.rs.world.Hit.HitLook;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public class Tarn extends NPC {

    public Tarn(final int id, final WorldTile tile, final int mapAreaNameHash,
                final boolean canBeAttackFromOutOfArea, final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
        setCapDamage(750);
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
        if (hit.getLook() != HitLook.MELEE_DAMAGE
                && hit.getLook() != HitLook.RANGE_DAMAGE
                && hit.getLook() != HitLook.MAGIC_DAMAGE)
            return;
        if (hit.getSource() != null) {
            final int recoil = (int) (hit.getDamage() * 0.2);
            if (recoil > 0) {
                hit.getSource().applyHit(
                        new Hit(this, recoil, HitLook.REFLECTED_DAMAGE));
            }
        }
    }

}