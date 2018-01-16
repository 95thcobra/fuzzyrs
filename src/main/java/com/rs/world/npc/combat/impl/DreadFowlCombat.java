package com.rs.world.npc.combat.impl;

import com.rs.core.utils.Utils;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.npc.familiar.Familiar;

public class DreadFowlCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{6825, 6824};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final Familiar familiar = (Familiar) npc;
        final boolean usingSpecial = familiar.hasSpecialOn();
        if (usingSpecial) {// priority over regular attack
            npc.setNextAnimation(new Animation(7810));
            npc.setNextGraphics(new Graphics(1318));
            CombatScript.delayHit(
                    npc,
                    1,
                    target,
                    CombatScript.getMagicHit(
                            npc,
                            CombatScript.getRandomMaxHit(npc, 40, NPCCombatDefinitions.MAGE,
                                    target)));
            World.sendProjectile(npc, target, 1376, 34, 16, 30, 35, 16, 0);
        } else {
            if (Utils.getRandom(10) == 0) {// 1/10 chance of random special
                // (weaker)
                npc.setNextAnimation(new Animation(7810));
                npc.setNextGraphics(new Graphics(1318));
                CombatScript.delayHit(
                        npc,
                        1,
                        target,
                        CombatScript.getMagicHit(
                                npc,
                                CombatScript.getRandomMaxHit(npc, 30,
                                        NPCCombatDefinitions.MAGE, target)));
                World.sendProjectile(npc, target, 1376, 34, 16, 30, 35, 16, 0);
            } else {
                npc.setNextAnimation(new Animation(7810));
                CombatScript.delayHit(
                        npc,
                        1,
                        target,
                        CombatScript.getMeleeHit(
                                npc,
                                CombatScript.getRandomMaxHit(npc, 30,
                                        NPCCombatDefinitions.MELEE, target)));
            }
        }
        return defs.getAttackDelay();
    }
}
