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
 * Handles the super dragonfire attack.
 *
 * @author Emperor
 */
public final class SuperFireAttack implements QueenAttack {

    /**
     * The animation.
     */
    private static final Animation ANIMATION = new Animation(16745);

    /**
     * The graphics.
     */
    private static final Graphics GRAPHIC = new Graphics(3152);

    @Override
    public int attack(final QueenBlackDragon npc, final Player victim) {
        npc.setNextAnimation(ANIMATION);
        npc.setNextGraphics(GRAPHIC);
        victim.getPackets()
                .sendGameMessage(
                        "<col=FFCC00>The Queen Black Dragon gathers her strength to breath extremely hot flames.</col>");
        WorldTasksManager.schedule(new WorldTask() {
            int count = 0;

            @Override
            public void run() {
                final String message = FireBreathAttack
                        .getProtectMessage(victim);
                int hit;
                if (message != null) {
                    hit = Utils.random(150 + Utils.random(120),
                            message.contains("prayer") ? 480 : 342);
                    victim.getPackets().sendGameMessage(message);
                } else {
                    hit = Utils.random(400, 798);
                    victim.getPackets().sendGameMessage(
                            "You are horribly burned by the dragon's breath!");
                }
                final int distance = Utils.getDistance(
                        npc.getBase().transform(33, 31, 0), victim);
                hit /= (distance / 3) + 1;
                victim.setNextAnimation(new Animation(Combat
                        .getDefenceEmote(victim)));
                victim.applyHit(new Hit(npc, hit, HitLook.REGULAR_DAMAGE));
                if (++count == 3) {
                    stop();
                }
            }
        }, 4, 1);
        return Utils.random(8, 15);
    }

    @Override
    public boolean canAttack(final QueenBlackDragon npc, final Player victim) {
        return true;
    }

}