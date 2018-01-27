package com.rs.world.npc.combat.impl;

import com.rs.utils.Utils;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.ForceTalk;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class OrkLegionCombat extends CombatScript {

    public String[] messages = {"For Bork!", "Die Human!", "To the attack!",
            "All together now!"};

    @Override
    public Object[] getKeys() {
        return new Object[]{"Ork legion"};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions cdef = npc.getCombatDefinitions();
        npc.setNextAnimation(new Animation(cdef.getAttackEmote()));
        if (Utils.getRandom(3) == 0) {
            npc.setNextForceTalk(new ForceTalk(messages[Utils
                    .getRandom(messages.length > 3 ? 3 : 0)]));
        }
        CombatScript.delayHit(npc, 0, target, CombatScript.getMeleeHit(npc, cdef.getMaxHit()));
        return cdef.getAttackDelay();
    }

}
