package com.rs.world.npc.combat.impl;

import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class DharokCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{2026};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        npc.setNextAnimation(new Animation(defs.getAttackEmote()));
        int damage = getRandomMaxHit(npc, defs.getMaxHit(),
                NPCCombatDefinitions.MELEE, target);
        if (damage != 0) {
            final double perc = 1 - (npc.getHitpoints() / npc.getMaxHitpoints());
            damage += perc * 380;
        }
        CombatScript.delayHit(npc, 0, target, getMeleeHit(npc, damage));
        return defs.getAttackDelay();
    }
}
