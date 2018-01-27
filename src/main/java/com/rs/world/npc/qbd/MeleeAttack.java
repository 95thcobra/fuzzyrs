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
 * Handles the Queen Black Dragon's melee attack.
 *
 * @author Emperor
 */
public final class MeleeAttack implements QueenAttack {

    /**
     * The default melee animation.
     */
    private static final Animation DEFAULT = new Animation(16717);

    /**
     * The east melee animation.
     */
    private static final Animation EAST = new Animation(16744);

    /**
     * The west melee animation.
     */
    private static final Animation WEST = new Animation(16743);

    @Override
    public int attack(final QueenBlackDragon npc, final Player victim) {
        if (victim.getX() < npc.getBase().getX() + 31) {
            npc.setNextAnimation(WEST);
        } else if (victim.getX() > npc.getBase().getX() + 35) {
            npc.setNextAnimation(EAST);
        } else {
            npc.setNextAnimation(DEFAULT);
        }
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                stop();
                int hit = 0;
                if (victim.getPrayer().usingPrayer(1, 9)) {
                    victim.setNextAnimation(new Animation(12573));
                    victim.setNextGraphics(new Graphics(2230));
                    victim.getPackets()
                            .sendGameMessage(
                                    "You are unable to reflect damage back to this creature.");
                    hit = 0;
                } else if (victim.getPrayer().usingPrayer(0, 19)) {
                    victim.setNextAnimation(new Animation(Combat
                            .getDefenceEmote(victim)));
                    hit = 0;
                } else {
                    hit = Utils.random(Utils.random(150), 360);
                    victim.setNextAnimation(new Animation(Combat
                            .getDefenceEmote(victim)));
                }
                victim.applyHit(new Hit(npc, hit, hit == 0 ? HitLook.MISSED
                        : HitLook.MELEE_DAMAGE));
            }
        });
        return Utils.random(4, 15);
    }

    @Override
    public boolean canAttack(final QueenBlackDragon npc, final Player victim) {
        return victim.getY() > npc.getBase().getY() + 32;
    }

}