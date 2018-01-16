package com.rs.world.npc.combat.impl;

import com.rs.core.utils.Utils;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.npc.familiar.Familiar;

public class GeyserTitanCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{7340, 7339};
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
            npc.setNextAnimation(new Animation(7883));
            npc.setNextGraphics(new Graphics(1373));
            if (distant) {// range hit
                if (Utils.getRandom(2) == 0) {
                    CombatScript.delayHit(
                            npc,
                            1,
                            target,
                            getRangeHit(
                                    npc,
                                    getRandomMaxHit(npc, 300,
                                            NPCCombatDefinitions.RANGE, target)));
                } else {
                    CombatScript.delayHit(
                            npc,
                            1,
                            target,
                            getMagicHit(
                                    npc,
                                    getRandomMaxHit(npc, 300,
                                            NPCCombatDefinitions.MAGE, target)));
                }
            } else {// melee hit
                CombatScript.delayHit(
                        npc,
                        1,
                        target,
                        getMeleeHit(
                                npc,
                                getRandomMaxHit(npc, 300,
                                        NPCCombatDefinitions.MELEE, target)));
            }
            World.sendProjectile(npc, target, 1376, 34, 16, 30, 35, 16, 0);
        } else {
            if (distant) {// range
                damage = getRandomMaxHit(npc, 244, NPCCombatDefinitions.RANGE,
                        target);
                npc.setNextAnimation(new Animation(7883));
                npc.setNextGraphics(new Graphics(1375));
                World.sendProjectile(npc, target, 1374, 34, 16, 30, 35, 16, 0);
                CombatScript.delayHit(npc, 2, target, getRangeHit(npc, damage));
            } else {// melee
                damage = getRandomMaxHit(npc, 244, NPCCombatDefinitions.MELEE,
                        target);
                npc.setNextAnimation(new Animation(7879));
                CombatScript.delayHit(npc, 1, target, getMeleeHit(npc, damage));
            }
        }
        return defs.getAttackDelay();
    }
}
