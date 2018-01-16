package com.rs.world.npc.qbd;

import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.player.combat.Combat;
import com.rs.world.Animation;
import com.rs.world.Graphics;
import com.rs.world.Hit;
import com.rs.world.Hit.HitLook;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

/**
 * Represents a default fire breath attack.
 *
 * @author Emperor
 */
public final class FireBreathAttack implements QueenAttack {

    /**
     * The animation of the attack.
     */
    private static final Animation ANIMATION = new Animation(16721);

    /**
     * The graphic of the attack.
     */
    private static final Graphics GRAPHIC = new Graphics(3143);

    /**
     * Gets the dragonfire protect message.
     *
     * @param player The player.
     * @return The message to send, or {@code null} if the player was
     * unprotected.
     */
    public static String getProtectMessage(final Player player) {
        if (Combat.hasAntiDragProtection(player))
            return "Your shield absorbs most of the dragon's breath!";
        if (player.getFireImmune() > Utils.currentTimeMillis())
            return "Your potion absorbs most of the dragon's breath!";
        if (player.getPrayer().usingPrayer(0, 17)
                || player.getPrayer().usingPrayer(1, 7))
            return "Your prayer absorbs most of the dragon's breath!";
        return null;
    }

    @Override
    public int attack(final QueenBlackDragon npc, final Player victim) {
        npc.setNextAnimation(ANIMATION);
        npc.setNextGraphics(GRAPHIC);
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                super.stop();
                final String message = getProtectMessage(victim);
                int hit;
                if (message != null) {
                    hit = Utils.random(60 + Utils.random(150),
                            message.contains("prayer") ? 460 : 235);
                    victim.getPackets().sendGameMessage(message);
                } else {
                    hit = Utils.random(400, 710);
                    victim.getPackets().sendGameMessage(
                            "You are horribly burned by the dragon's breath!");
                }
                victim.setNextAnimation(new Animation(Combat
                        .getDefenceEmote(victim)));
                victim.applyHit(new Hit(npc, hit, HitLook.REGULAR_DAMAGE));
            }
        }, 1);
        return Utils.random(4, 15); // Attack delay seems to be random a lot.
    }

    @Override
    public boolean canAttack(final QueenBlackDragon npc, final Player victim) {
        return true;
    }
}