package com.rs.world.npc.qbd;

import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.player.combat.Combat;
import com.rs.world.Animation;
import com.rs.world.Graphics;
import com.rs.world.Hit;
import com.rs.world.Hit.HitLook;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

/**
 * Handles the Queen Black Dragon's range attack.
 *
 * @author Emperor
 */
public final class RangeAttack implements QueenAttack {

    /**
     * The animation.
     */
    private static final Animation ANIMATION = new Animation(16718);

    @Override
    public int attack(final QueenBlackDragon npc, final Player victim) {
        npc.setNextAnimation(ANIMATION);
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                stop();
                int hit;
                if (victim.getPrayer().usingPrayer(1, 8)) {
                    victim.setNextAnimation(new Animation(12573));
                    victim.setNextGraphics(new Graphics(2229));
                    victim.getPackets()
                            .sendGameMessage(
                                    "You are unable to reflect damage back to this creature.");
                    hit = 0;
                } else if (victim.getPrayer().usingPrayer(0, 18)) {
                    victim.setNextAnimation(new Animation(Combat
                            .getDefenceEmote(victim)));
                    hit = 0;
                } else {
                    hit = Utils.random(0 + Utils.random(150), 360);
                    victim.setNextAnimation(new Animation(Combat
                            .getDefenceEmote(victim)));
                }
                victim.applyHit(new Hit(npc, hit, hit == 0 ? HitLook.MISSED
                        : HitLook.RANGE_DAMAGE));
            }
        }, 1);
        return Utils.random(4, 15);
    }

    @Override
    public boolean canAttack(final QueenBlackDragon npc, final Player victim) {
        return true;
    }

}