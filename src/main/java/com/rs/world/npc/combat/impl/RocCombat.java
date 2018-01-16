package com.rs.world.npc.combat.impl;

import com.rs.core.utils.Utils;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.Graphics;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class RocCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{4972};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        if (Utils.getRandom(2) == 0) { // ranged wind
            npc.setNextAnimation(new Animation(5031));
            for (final Entity t : npc.getPossibleTargets()) {
                CombatScript.delayHit(
                        npc,
                        1,
                        t,
                        getRangeHit(
                                npc,
                                getRandomMaxHit(npc, 699,
                                        NPCCombatDefinitions.RANGE, t)));
                t.setNextGraphics(new Graphics(2196));
            }
        } else { // attacking with melee
            npc.setNextAnimation(new Animation(defs.getAttackEmote()));
            CombatScript.delayHit(
                    npc,
                    0,
                    target,
                    getMeleeHit(
                            npc,
                            getRandomMaxHit(npc, defs.getMaxHit(),
                                    NPCCombatDefinitions.MELEE, target)));
        }
        return defs.getAttackDelay();
    }
}
