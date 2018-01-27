package com.rs.world.npc.combat.impl;

import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.Graphics;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.npc.familiar.Familiar;

public class SpiritKalphiteCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{6995, 6994};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final Familiar familiar = (Familiar) npc;
        final boolean usingSpecial = familiar.hasSpecialOn();
        int damage = 0;
        if (usingSpecial) {// TODO find special
            npc.setNextAnimation(new Animation(8519));
            npc.setNextGraphics(new Graphics(8519));
            damage = getRandomMaxHit(npc, 20, NPCCombatDefinitions.MELEE,
                    target);
            delayHit(npc, 1, target, getMeleeHit(npc, damage));
        } else {
            npc.setNextAnimation(new Animation(8519));
            damage = getRandomMaxHit(npc, 50, NPCCombatDefinitions.MELEE,
                    target);
            delayHit(npc, 1, target, getMeleeHit(npc, damage));
        }
        return defs.getAttackDelay();
    }
}
