package com.rs.world.npc.combat.impl;

import com.rs.core.utils.Utils;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.Graphics;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class GuthanCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{2027};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        npc.setNextAnimation(new Animation(defs.getAttackEmote()));
        final int damage = getRandomMaxHit(npc, defs.getMaxHit(),
                NPCCombatDefinitions.MELEE, target);
        if (damage != 0 && Utils.random(3) == 0) {
            target.setNextGraphics(new Graphics(398));
            npc.heal(damage);
        }
        CombatScript.delayHit(npc, 0, target, getMeleeHit(npc, damage));
        return defs.getAttackDelay();
    }
}
