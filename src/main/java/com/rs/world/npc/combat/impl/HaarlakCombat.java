package com.rs.world.npc.combat.impl;

import com.rs.core.utils.Utils;
import com.rs.world.*;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class HaarlakCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{9911};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        if (Utils.getRandom(1) == 0) { // mage attack
            npc.setCapDamage(1000);
            npc.setNextAnimation(new Animation(14371));
            npc.setNextForceTalk(new ForceTalk("Feel the Pain of Water!"));
            for (final Entity t : npc.getPossibleTargets()) {
                if (!t.withinDistance(npc, 18)) {
                    continue;
                }
                final int damage = CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                        NPCCombatDefinitions.MAGE, t);
                if (damage > 0) {
                    CombatScript.delayHit(npc, 1, t, CombatScript.getMagicHit(npc, damage));
                    World.sendProjectile(npc, t, 500, 50, 16, 41, 35, 16, 0);
                    t.setNextGraphics(new Graphics(502));
                }
            }

        } else { // melee attack
            npc.setNextAnimation(new Animation(defs.getAttackEmote()));
            npc.setCapDamage(1000);
            target.setNextGraphics(new Graphics(556));
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