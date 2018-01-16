package com.rs.world.npc.others;

import com.rs.world.Graphics;
import com.rs.world.Hit;
import com.rs.world.Hit.HitLook;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public class MercenaryMage extends NPC {

    public MercenaryMage(final int id, final WorldTile tile,
                         final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea,
                         final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
        setLureDelay(0);
        setCapDamage(2500);
        setCombatLevel(50000);
        setName("<col=4169E1>Cap'n</col> Dard");
        setRun(true);
        setForceMultiAttacked(true);
        setForceAgressive(true);
    }

    @Override
    public double getMagePrayerMultiplier() {
        return 0.5;
    }

    @Override
    public double getRangePrayerMultiplier() {
        return 0.5;
    }

    @Override
    public double getMeleePrayerMultiplier() {
        return 0.5;
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        super.handleIngoingHit(hit);
        if (hit.getLook() != HitLook.MELEE_DAMAGE
                && hit.getLook() != HitLook.RANGE_DAMAGE
                && hit.getLook() != HitLook.MAGIC_DAMAGE)
            return;
        if (hit.getSource() != null) {
            final int recoil = hit.getDamage() / 2;
            if (recoil > 0) {
                hit.getSource().applyHit(
                        new Hit(this, recoil, HitLook.REFLECTED_DAMAGE));
                setNextGraphics(new Graphics(2180));
            }
        }
    }

}
