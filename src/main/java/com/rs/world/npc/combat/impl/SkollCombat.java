package com.rs.world.npc.combat.impl;

import com.rs.utils.Utils;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.ForceTalk;
import com.rs.world.Graphics;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

/**
 * Sk�ll
 *
 * @author BongoProd on Rune-Server.>
 */

public class SkollCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{14836};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        if (Utils.getRandom(1) == 0) { // Hati mage attack
            npc.setNextAnimation(new Animation(15257));
            npc.setNextForceTalk(new ForceTalk(
                    "Ikla Homla Sunna, kill them all!"));
            for (final Entity t : npc.getPossibleTargets()) {
                if (!t.withinDistance(npc, 18)) {
                    continue;
                }
                final int damage = getRandomMaxHit(npc, defs.getMaxHit(),
                        NPCCombatDefinitions.RANGE, t);
                if (damage > 0) {
                    CombatScript.delayHit(npc, 1, t, getRangeHit(npc, damage));
                    t.setNextGraphics(new Graphics(3000)); // boom
                }
            }

        } else { // Hati melee attack
            npc.setNextAnimation(new Animation(defs.getAttackEmote()));
            npc.setNextForceTalk(new ForceTalk(
                    "Filka Dovla Toota, I will destroy you, like I destroyed Jake!"));
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