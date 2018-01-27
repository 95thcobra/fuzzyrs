package com.rs.world.npc.combat.impl;

import com.rs.utils.Utils;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.Graphics;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

/**
 * Hati real boss.
 *
 * @author BongoProd on Rune-Server.>
 */

public class HatiCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{13460};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        if (Utils.getRandom(1) == 0) { // Hati mage attack
            npc.setNextAnimation(new Animation(15257));
            npc.setCapDamage(750);
            for (final Entity t : npc.getPossibleTargets()) {
                if (!t.withinDistance(npc, 18)) {
                    continue;
                }
                final int damage = CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                        NPCCombatDefinitions.MAGE, t);
                if (damage > 0) {
                    CombatScript.delayHit(npc, 1, t, CombatScript.getMagicHit(npc, damage));
                    t.setNextGraphics(new Graphics(2854)); // used blink gfx, I
                    // haven't found the
                    // real gfx yet.
                }
            }

        } else { // Hati melee attack
            npc.setCapDamage(750);
            npc.setNextAnimation(new Animation(defs.getAttackEmote()));
            CombatScript.delayHit(
                    npc,
                    0,
                    target,
                    CombatScript.getMeleeHit(
                            npc,
                            CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                                    NPCCombatDefinitions.MELEE, target)));
        }
        return defs.getAttackDelay();
    }
}