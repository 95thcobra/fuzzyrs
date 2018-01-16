package com.rs.world.npc.others;

import com.rs.world.Hit;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public class Lucien extends NPC {

    public Lucien(final int id, final WorldTile tile,
                  final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea,
                  final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
        setLureDelay(0);
        setCapDamage(750);
        setCombatLevel(18399);
        setRun(true);
        setForceMultiAttacked(true);
    }

    @Override
    public double getMagePrayerMultiplier() {
        return 0.3;
    }

    @Override
    public double getRangePrayerMultiplier() {
        return 0.3;
    }

    @Override
    public double getMeleePrayerMultiplier() {
        return 0.3;
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        if (hit.getLook() != Hit.HitLook.MELEE_DAMAGE
                && hit.getLook() != Hit.HitLook.RANGE_DAMAGE
                && hit.getLook() != Hit.HitLook.MAGIC_DAMAGE)
            return;
        super.handleIngoingHit(hit);
        if (hit.getSource() != null) {
            final int recoil = (int) (hit.getDamage() * 0.1);
            if (recoil > 0) {
                hit.getSource().applyHit(
                        new Hit(this, recoil, Hit.HitLook.REFLECTED_DAMAGE));
            }
        }
    }
}
