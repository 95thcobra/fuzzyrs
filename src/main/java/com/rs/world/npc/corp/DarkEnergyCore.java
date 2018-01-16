package com.rs.world.npc.corp;

import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Entity;
import com.rs.world.Hit;
import com.rs.world.Hit.HitLook;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class DarkEnergyCore extends NPC {

    private final CorporealBeast beast;
    private Entity target;
    private int changeTarget;
    private int delay;

    public DarkEnergyCore(final CorporealBeast beast) {
        super(8127, beast, -1, true, true);
        setForceMultiArea(true);
        this.beast = beast;
        changeTarget = 2;
    }

    @Override
    public void processNPC() {
        if (isDead() || hasFinished())
            return;
        if (delay > 0) {
            delay--;
            return;
        }
        if (changeTarget > 0) {
            if (changeTarget == 1) {
                final ArrayList<Entity> possibleTarget = beast
                        .getPossibleTargets();
                if (possibleTarget.isEmpty()) {
                    finish();
                    beast.removeDarkEnergyCore();
                    return;
                }
                target = possibleTarget.get(Utils.getRandom(possibleTarget
                        .size() - 1));
                setNextWorldTile(new WorldTile(target));
                World.sendProjectile(this, this, target, 1828, 0, 0, 40, 40,
                        20, 0);
            }
            changeTarget--;
            return;
        }
        if (target == null || target.getX() != getX()
                || target.getY() != getY() || target.getPlane() != getPlane()) {
            changeTarget = 5;
            return;
        }
        final int damage = Utils.getRandom(50) + 50;
        target.applyHit(new Hit(this, Utils.random(1, 131),
                HitLook.MAGIC_DAMAGE));
        beast.heal(damage);
        delay = getPoison().isPoisoned() ? 10 : 3;
        if (target instanceof Player) {
            final Player player = (Player) target;
            player.getPackets()
                    .sendGameMessage(
                            "The dark core creature steals some life from you for its master.");
        }
    }

    @Override
    public double getMagePrayerMultiplier() {
        return 0.6;
    }

    @Override
    public void sendDeath(final Entity source) {
        super.sendDeath(source);
        beast.removeDarkEnergyCore();
    }

}
