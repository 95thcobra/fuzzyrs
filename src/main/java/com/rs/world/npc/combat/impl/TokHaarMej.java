package com.rs.world.npc.combat.impl;

import com.rs.utils.Utils;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class TokHaarMej extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{15203};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final int distanceX = target.getX() - npc.getX();
        final int distanceY = target.getY() - npc.getY();
        final int size = npc.getSize();
        int hit = 0;
        int attackStyle = Utils.random(2);
        if (attackStyle == 0
                && (distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1)) {
            attackStyle = 1;
        }
        switch (attackStyle) {
            case 0:
                hit = CombatScript.getRandomMaxHit(npc, defs.getMaxHit() - 36,
                        NPCCombatDefinitions.MELEE, target);
                npc.setNextAnimation(new Animation(defs.getAttackEmote()));
                CombatScript.delayHit(npc, 0, target, CombatScript.getMeleeHit(npc, hit));
                break;
            case 1:
                hit = CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                        NPCCombatDefinitions.MAGE, target);
                npc.setNextAnimation(new Animation(16122));
                World.sendProjectile(npc, target, 2991, 34, 16, 30, 35, 16, 0);
                CombatScript.delayHit(npc, 2, target, CombatScript.getMagicHit(npc, hit));
                break;
        }
        return defs.getAttackDelay();
    }
}
