package com.rs.world.npc.qbd;

import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Graphics;
import com.rs.world.Hit;
import com.rs.world.Hit.HitLook;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.Iterator;

/**
 * The Queen Black Dragon's soul siphon attack.
 *
 * @author Emperor
 */
public final class SoulSiphonAttack implements QueenAttack {

    /**
     * The siphon graphics.
     */
    private static final Graphics SIPHON_GRAPHIC = new Graphics(3148);

    @Override
    public int attack(final QueenBlackDragon npc, final Player victim) {
        for (final Iterator<TorturedSoul> it = npc.getSouls().iterator(); it
                .hasNext(); ) {
            final TorturedSoul soul = it.next();
            if (soul.isDead()) {
                it.remove();
            }
        }
        if (npc.getSouls().isEmpty())
            return 1;
        victim.getPackets()
                .sendGameMessage(
                        "<col=9900CC>The Queen Black Dragon starts to siphon the energy of her mages.</col>");
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                for (final Iterator<TorturedSoul> it = npc.getSouls()
                        .iterator(); it.hasNext(); ) {
                    final TorturedSoul soul = it.next();
                    if (soul.isDead()) {
                        it.remove();
                        continue;
                    }
                    soul.setNextGraphics(SIPHON_GRAPHIC);
                    soul.applyHit(new Hit(npc, 20, HitLook.REGULAR_DAMAGE));
                    npc.getNextHits().add(
                            new Hit(npc, 40, HitLook.HEALED_DAMAGE));
                    npc.heal(40);
                }
                if (npc.getSouls().isEmpty()) {
                    stop();
                    npc.getTemporaryAttributtes().put("_last_soul_summon",
                            npc.getTicks() + Utils.random(120) + 125);
                }
            }
        }, 0, 0);
        npc.getTemporaryAttributtes().put("_last_soul_summon",
                npc.getTicks() + 999);
        npc.getTemporaryAttributtes().put("_soul_siphon_atk",
                npc.getTicks() + 50 + Utils.random(40));
        return Utils.random(5, 10);
    }

    @Override
    public boolean canAttack(final QueenBlackDragon npc, final Player victim) {
        final Integer tick = (Integer) npc.getTemporaryAttributtes().get(
                "_soul_siphon_atk");
        return tick == null || tick < npc.getTicks();
    }

}