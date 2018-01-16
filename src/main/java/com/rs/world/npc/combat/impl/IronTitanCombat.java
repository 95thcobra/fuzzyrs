package com.rs.world.npc.combat.impl;

import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.npc.familiar.Familiar;

public class IronTitanCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{7376, 7375};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final int distanceX = target.getX() - npc.getX();
        final int distanceY = target.getY() - npc.getY();
        boolean distant = false;
        final int size = npc.getSize();
        final Familiar familiar = (Familiar) npc;
        final boolean usingSpecial = familiar.hasSpecialOn();
        int damage = 0;
        if (distanceX > size || distanceX < -1 || distanceY > size
                || distanceY < -1) {
            distant = true;
        }
        if (usingSpecial) {// priority over regular attack
            npc.setNextAnimation(new Animation(7954));
            npc.setNextGraphics(new Graphics(1450));
            if (distant) {// range hit
                CombatScript.delayHit(
                        npc,
                        2,
                        target,
                        CombatScript.getMagicHit(
                                npc,
                                CombatScript.getRandomMaxHit(npc, 220,
                                        NPCCombatDefinitions.MAGE, target)),
                        CombatScript.getMagicHit(
                                npc,
                                CombatScript.getRandomMaxHit(npc, 220,
                                        NPCCombatDefinitions.MAGE, target)),
                        CombatScript.getMagicHit(
                                npc,
                                CombatScript.getRandomMaxHit(npc, 220,
                                        NPCCombatDefinitions.MAGE, target)));
            } else {// melee hit
                CombatScript.delayHit(
                        npc,
                        1,
                        target,
                        CombatScript.getMeleeHit(
                                npc,
                                CombatScript.getRandomMaxHit(npc, 230,
                                        NPCCombatDefinitions.MELEE, target)),
                        CombatScript.getMeleeHit(
                                npc,
                                CombatScript.getRandomMaxHit(npc, 230,
                                        NPCCombatDefinitions.MELEE, target)),
                        CombatScript.getMeleeHit(
                                npc,
                                CombatScript.getRandomMaxHit(npc, 230,
                                        NPCCombatDefinitions.MELEE, target)));
            }
        } else {
            if (distant) {
                damage = CombatScript.getRandomMaxHit(npc, 255, NPCCombatDefinitions.MAGE,
                        target);
                npc.setNextAnimation(new Animation(7694));
                World.sendProjectile(npc, target, 1452, 34, 16, 30, 35, 16, 0);
                CombatScript.delayHit(npc, 2, target, CombatScript.getMagicHit(npc, damage));
            } else {// melee
                damage = CombatScript.getRandomMaxHit(npc, 244, NPCCombatDefinitions.MELEE,
                        target);
                npc.setNextAnimation(new Animation(7946));
                npc.setNextGraphics(new Graphics(1447));
                CombatScript.delayHit(npc, 1, target, CombatScript.getMeleeHit(npc, damage));
            }
        }
        return defs.getAttackDelay();
    }

}
