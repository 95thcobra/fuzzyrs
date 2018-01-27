package com.rs.world.npc.combat.impl;

import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.npc.familiar.Familiar;

public class ThornySnailCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{6807, 6806};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final Familiar familiar = (Familiar) npc;
        final boolean usingSpecial = familiar.hasSpecialOn();
        if (usingSpecial) {// priority over regular attack
            npc.setNextAnimation(new Animation(8148));
            npc.setNextGraphics(new Graphics(1385));
            World.sendProjectile(npc, target, 1386, 34, 16, 30, 35, 16, 0);
            CombatScript.delayHit(
                    npc,
                    1,
                    target,
                    CombatScript.getRangeHit(
                            npc,
                            CombatScript.getRandomMaxHit(npc, 80,
                                    NPCCombatDefinitions.RANGE, target)));
            npc.setNextGraphics(new Graphics(1387));
        } else {
            npc.setNextAnimation(new Animation(8143));
            CombatScript.delayHit(
                    npc,
                    1,
                    target,
                    CombatScript.getRangeHit(
                            npc,
                            CombatScript.getRandomMaxHit(npc, 40,
                                    NPCCombatDefinitions.RANGE, target)));
        }
        return defs.getAttackDelay();
    }

}
