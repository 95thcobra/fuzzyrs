package com.rs.world.npc.combat.impl;

import com.rs.core.utils.Utils;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.npc.others.TormentedDemon;

public class TormentedDemonCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{"Tormented demon"};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final TormentedDemon torm = (TormentedDemon) npc;
        int hit = 0;
        final int attackStyle = torm.getFixedAmount() == 0 ? Utils.getRandom(2)
                : torm.getFixedCombatType();
        if (torm.getFixedAmount() == 0) {
            torm.setFixedCombatType(attackStyle);
        }
        switch (attackStyle) {
            case 0:
                if (npc.withinDistance(target, 3)) {
                    hit = getRandomMaxHit(npc, 189, NPCCombatDefinitions.MELEE,
                            target);
                    npc.setNextAnimation(new Animation(10922));
                    npc.setNextGraphics(new Graphics(1886));
                    delayHit(npc, 1, target, getMeleeHit(npc, hit));
                }
                return defs.getAttackDelay();
            case 1:
                hit = getRandomMaxHit(npc, 270, NPCCombatDefinitions.MAGE, target);
                npc.setNextAnimation(new Animation(10918));
                npc.setNextGraphics(new Graphics(1883, 0, 96 << 16));
                World.sendProjectile(npc, target, 1884, 34, 16, 30, 35, 16, 0);
                delayHit(npc, 1, target, getMagicHit(npc, hit));
                break;
            case 2:
                hit = getRandomMaxHit(npc, 270, NPCCombatDefinitions.RANGE, target);
                npc.setNextAnimation(new Animation(10919));
                npc.setNextGraphics(new Graphics(1888));
                World.sendProjectile(npc, target, 1887, 34, 16, 30, 35, 16, 0);
                delayHit(npc, 1, target, getRangeHit(npc, hit));
                break;
        }
        torm.setFixedAmount(torm.getFixedAmount() + 1);
        return defs.getAttackDelay();
    }
}
