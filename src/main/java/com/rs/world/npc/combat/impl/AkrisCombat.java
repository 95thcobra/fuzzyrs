package com.rs.world.npc.combat.impl;

import com.rs.utils.Utils;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class AkrisCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{14297};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        if (Utils.getRandom(2) == 0) { // range magical attack
            npc.setNextGraphics(new Graphics(2728));
            npc.setNextAnimation(new Animation(2791));
            for (final Entity t : npc.getPossibleTargets()) {
                CombatScript.delayHit(
                        npc,
                        1,
                        t,
                        CombatScript.getRangeHit(
                                npc,
                                CombatScript.getRandomMaxHit(npc, 500,
                                        NPCCombatDefinitions.RANGE, t)));
                World.sendProjectile(npc, target, 2735, 18, 18, 50, 50, 3, 0);
                World.sendProjectile(npc, target, 2736, 18, 18, 50, 50, 20, 0);
                World.sendProjectile(npc, target, 2736, 18, 18, 50, 50, 110, 0);
            }
        } else { // melee attack
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