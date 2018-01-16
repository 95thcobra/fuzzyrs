package com.rs.world.npc.combat.impl;

import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class LivingRockStrickerCombat extends CombatScript {

    @Override
    public Object[] getKeys() {

        return new Object[]{8833};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final int distanceX = target.getX() - npc.getX();
        final int distanceY = target.getY() - npc.getY();
        final int size = npc.getSize();
        if (distanceX > size || distanceX < -1 || distanceY > size
                || distanceY < -1) {
            // TODO add projectile
            npc.setNextAnimation(new Animation(12196));
            CombatScript.delayHit(
                    npc,
                    1,
                    target,
                    CombatScript.getRangeHit(
                            npc,
                            CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                                    NPCCombatDefinitions.RANGE, target)));
        } else {
            npc.setNextAnimation(new Animation(defs.getAttackEmote()));
            CombatScript.delayHit(
                    npc,
                    0,
                    target,
                    CombatScript.getMeleeHit(
                            npc,
                            CombatScript.getRandomMaxHit(npc, 84,
                                    NPCCombatDefinitions.MELEE, target)));
            return defs.getAttackDelay();
        }

        return defs.getAttackDelay();
    }

}
