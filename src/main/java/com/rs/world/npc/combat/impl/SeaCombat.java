package com.rs.world.npc.combat.impl;

import com.rs.utils.Utils;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.ForceTalk;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class SeaCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{3847};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        if (Utils.getRandom(8) == 0) {
            switch (Utils.getRandom(0)) {
                case 0:
                    npc.setNextForceTalk(new ForceTalk("I will eat you all!"));
                    break;
            }
        }
        if (Utils.getRandom(2) == 0) { // mage attack
            npc.setNextAnimation(new Animation(3992));
            for (final Entity t : npc.getPossibleTargets()) {
                CombatScript.delayHit(
                        npc,
                        1,
                        t,
                        CombatScript.getMagicHit(
                                npc,
                                CombatScript.getRandomMaxHit(npc, 700,
                                        NPCCombatDefinitions.MAGE, t)));
                World.sendProjectile(npc, t, 2707, 18, 18, 50, 50, 3, 0);
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
