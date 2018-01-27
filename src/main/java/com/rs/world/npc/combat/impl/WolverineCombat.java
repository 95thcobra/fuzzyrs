package com.rs.world.npc.combat.impl;

import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.ForceTalk;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class WolverineCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{15126};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final Player player = (Player) target;
        npc.setNextAnimation(new Animation(10961));
        npc.setCapDamage(1000);
        npc.setNextForceTalk(new ForceTalk(
                "Claws of the Elements, do your worst!"));
        final int damage = CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                NPCCombatDefinitions.MELEE, target);
        final int dclaw1 = damage / 2;
        final int dclaw2 = damage / 3;
        final int dclaw3 = damage / 3;
        CombatScript.delayHit(npc, 2, target, CombatScript.getMagicHit(npc, damage));
        CombatScript.delayHit(npc, 2, target, CombatScript.getRangeHit(npc, dclaw1));
        CombatScript.delayHit(npc, 2, target, CombatScript.getMeleeHit(npc, dclaw2));
        CombatScript.delayHit(npc, 2, target, CombatScript.getMeleeHit(npc, dclaw3));
        return defs.getAttackDelay();
    }
}
