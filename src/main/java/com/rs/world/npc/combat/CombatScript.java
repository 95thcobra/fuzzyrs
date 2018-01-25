package com.rs.world.npc.combat;

import com.rs.content.actions.skills.Skills;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.player.combat.CombatDefinitions;
import com.rs.player.combat.PlayerCombat;
import com.rs.world.Entity;
import com.rs.world.Hit;
import com.rs.world.npc.NPC;
import com.rs.world.npc.familiar.Steeltitan;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.Arrays;

public abstract class CombatScript {

    public static void delayHit(final NPC npc, final int delay,
                                final Entity target, final Hit... hits) {
        npc.getCombat().addAttackedByDelay(target);
        WorldTasksManager.schedule(new WorldTask() {

            @Override
            public void run() {
                System.out.println("Hits processed: " + Arrays.toString(hits));
                for (final Hit hit : hits) {
                    final NPC npc = (NPC) hit.getSource();
                    if (npc.isDead() || npc.hasFinished() || target.isDead()
                            || target.hasFinished()) {
                        System.out.println("done with this fight delayHit()");
                        return;
                    }
                    target.applyHit(hit);
                    npc.getCombat().doDefenceEmote(target);
                    /*
                     * if (!(npc instanceof Nex) && hit.getLook() ==
                     * HitLook.MAGIC_DAMAGE && hit.getDamage() == 0)
                     * target.setNextGraphics(new Graphics(85, 0, 100));
                     */
                    if (target instanceof Player) {
                        final Player p2 = (Player) target;
                        p2.closeInterfaces();
                        if (p2.getCombatDefinitions().isAutoRelatie()
                                && !p2.getActionManager().hasSkillWorking()
                                && !p2.hasWalkSteps()) {
                            p2.getActionManager().setAction(
                                    new PlayerCombat(npc));
                        }
                    } else {
                        final NPC n = (NPC) target;
                        if (!n.isUnderCombat()
                                || n.canBeAttackedByAutoRelatie()) {
                            n.setTarget(npc);
                        }
                    }

                }
            }

        }, delay);
    }

    public static Hit getRangeHit(final NPC npc, final int damage) {
        return new Hit(npc, damage, Hit.HitLook.RANGE_DAMAGE);
    }

    public static Hit getMagicHit(final NPC npc, final int damage) {
        return new Hit(npc, damage, Hit.HitLook.MAGIC_DAMAGE);
    }

    public static Hit getRegularHit(final NPC npc, final int damage) {
        return new Hit(npc, damage, Hit.HitLook.REGULAR_DAMAGE);
    }

    public static Hit getMeleeHit(final NPC npc, final int damage) {
        return new Hit(npc, damage, Hit.HitLook.MELEE_DAMAGE);
    }

    public static int getRandomMaxHit(final NPC npc, final int maxHit,
                                      final int attackStyle, final Entity target) {
        final int[] bonuses = npc.getBonuses();
        System.out.println("Bonuses for " + npc.getName() + ": " + Arrays.toString(bonuses));
        final double att = bonuses == null ? 0
                : attackStyle == NPCCombatDefinitions.RANGE ? bonuses[CombatDefinitions.RANGE_ATTACK]
                : attackStyle == NPCCombatDefinitions.MAGE ? bonuses[CombatDefinitions.MAGIC_ATTACK]
                : bonuses[CombatDefinitions.STAB_ATTACK];
        double def;
        if (target instanceof Player) {
            final Player p2 = (Player) target;
            def = p2.getSkills().getLevel(Skills.DEFENCE)
                    + (2 * p2.getCombatDefinitions().getBonuses()[attackStyle == NPCCombatDefinitions.RANGE ? CombatDefinitions.RANGE_DEF
                    : attackStyle == NPCCombatDefinitions.MAGE ? CombatDefinitions.MAGIC_DEF
                    : CombatDefinitions.STAB_DEF]);
            def *= p2.getPrayer().getDefenceMultiplier();
            if (attackStyle == NPCCombatDefinitions.MELEE) {
                if (p2.getFamiliar() instanceof Steeltitan) {
                    def *= 1.15;
                }
            }
        } else {
            final NPC n = (NPC) target;
            def = n.getBonuses() == null ? 0
                    : n.getBonuses()[attackStyle == NPCCombatDefinitions.RANGE ? CombatDefinitions.RANGE_DEF
                    : attackStyle == NPCCombatDefinitions.MAGE ? CombatDefinitions.MAGIC_DEF
                    : CombatDefinitions.STAB_DEF];
            def *= 2;
        }
        System.out.println("Attack: " + att);
        System.out.println("Defense: " + def);
        double prob = att / def;
        System.out.println("Probability of hit: " + prob);
        if (prob > 0.90) {
            prob = 0.90;
        } else if (prob < 0.05) {
            prob = 0.05;
        }
        if (prob < Math.random()) {
            System.out.println("What fucking idiot put this here?");
            return 0;
        }
        int finalHit = Utils.getRandom(maxHit);
        System.out.println("Hit registered: " + finalHit);
        return finalHit;
    }

    /*
     * Returns ids and names
     */
    public abstract Object[] getKeys();

    /*
     * Returns Move Delay
     */
    public abstract int attack(NPC npc, Entity target);

}
