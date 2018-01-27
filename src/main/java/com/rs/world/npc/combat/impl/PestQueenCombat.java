package com.rs.world.npc.combat.impl;

import com.rs.utils.Utils;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.ForceTalk;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class PestQueenCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{6358};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        if (Utils.getRandom(4) == 0) {
            switch (Utils.getRandom(0)) {
                case 0:
                    npc.setNextForceTalk(new ForceTalk(
                            "I'll give you a warm welcome... between my teeth!"));
                    break;
            }
        }
        if (Utils.getRandom(2) == 0) { // Melee attack 2
            npc.setNextAnimation(new Animation(14801));
            for (final Entity t : npc.getPossibleTargets()) {
                CombatScript.delayHit(
                        npc,
                        1,
                        target,
                        CombatScript.getMeleeHit(
                                npc,
                                CombatScript.getRandomMaxHit(npc, 500,
                                        NPCCombatDefinitions.MELEE, target)));
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
