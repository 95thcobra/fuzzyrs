package com.rs.world.npc.combat.impl;

import com.rs.core.utils.Utils;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.ForceTalk;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class AvatarCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{8596};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        if (Utils.getRandom(4) == 0) {
            switch (Utils.getRandom(2)) {
                case 0:
                    npc.setNextForceTalk(new ForceTalk(
                            "I'm back from 2012 MOTHAFUCKA!"));
                    break;
                case 1:
                    npc.setNextForceTalk(new ForceTalk("Gruuurghhhhh"));
                    break;
                case 2:
                    npc.setNextForceTalk(new ForceTalk(
                            "Minions, kill them! Wait... I haven't got any."));
                    break;
            }
        }
        if (Utils.getRandom(2) == 0) { // range magical attack
            npc.setNextAnimation(new Animation(defs.getAttackEmote()));
            target.getPoison().makePoisoned(120);
            CombatScript.delayHit(
                    npc,
                    0,
                    target,
                    CombatScript.getMeleeHit(
                            npc,
                            CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                                    NPCCombatDefinitions.MELEE, target)));
        } else { // melee attack
            npc.setNextAnimation(new Animation(defs.getAttackEmote()));
            target.getPoison().makePoisoned(120);
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
