package com.rs.world.npc.combat.impl;

import com.rs.core.utils.Utils;
import com.rs.world.*;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class IceKingCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{5472};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        if (Utils.getRandom(4) == 0) {
            switch (Utils.getRandom(1)) {
                case 0:
                    npc.setNextForceTalk(new ForceTalk(
                            "Ruurghh, welcome to my WORLD!"));
                    break;
                case 1:
                    npc.setNextForceTalk(new ForceTalk("Bwuuuuurrrrggggghhhhh!"));
                    break;
            }
        }
        if (Utils.getRandom(2) == 0) { // Mage Freeze Attack
            npc.setNextAnimation(new Animation(1931));
            for (final Entity t : npc.getPossibleTargets()) {
                CombatScript.delayHit(
                        npc,
                        1,
                        t,
                        CombatScript.getRangeHit(
                                npc,
                                CombatScript.getRandomMaxHit(npc, 550,
                                        NPCCombatDefinitions.RANGE, t)));
                World.sendProjectile(npc, target, 369, 28, 16, 35, 35, 16, 0);
            }
        } else { // melee attack
            npc.setNextAnimation(new Animation(defs.getAttackEmote()));
            target.setNextGraphics(new Graphics(2191));
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
