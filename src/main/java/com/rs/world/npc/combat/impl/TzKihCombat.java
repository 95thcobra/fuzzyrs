package com.rs.world.npc.combat.impl;

import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.npc.familiar.Familiar;

public class TzKihCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{"Tz-Kih", 7361, 7362};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {// yoa
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        int damage = 0;
        if (npc instanceof Familiar) {// TODO get anim and gfx
            final Familiar familiar = (Familiar) npc;
            final boolean usingSpecial = familiar.hasSpecialOn();
            if (usingSpecial) {
                for (final Entity entity : npc.getPossibleTargets()) {
                    damage = getRandomMaxHit(npc, 70,
                            NPCCombatDefinitions.MELEE, target);
                    if (target instanceof Player) {
                        ((Player) target).getPrayer().drainPrayer(damage);
                    }
                    CombatScript.delayHit(npc, 0, entity, getMeleeHit(npc, damage));
                }
            }
            return defs.getAttackDelay();
        }
        npc.setNextAnimation(new Animation(defs.getAttackEmote()));
        damage = getRandomMaxHit(npc, defs.getMaxHit(),
                NPCCombatDefinitions.MELEE, target);
        if (target instanceof Player) {
            ((Player) target).getPrayer().drainPrayer(damage + 10);
        }
        CombatScript.delayHit(npc, 0, target, getMeleeHit(npc, damage));
        return defs.getAttackDelay();
    }
}
