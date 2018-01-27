package com.rs.world.npc.combat;

import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.npc.NPC;

public class Default extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{"Default"};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final int attackStyle = defs.getAttackStyle();
        if (attackStyle == NPCCombatDefinitions.MELEE) {
            CombatScript.delayHit(
                    npc,
                    0,
                    target,
                    getMeleeHit(
                            npc,
                            getRandomMaxHit(npc, defs.getMaxHit(), attackStyle,
                                    target)));
        } else {
            final int damage = getRandomMaxHit(npc, defs.getMaxHit(),
                    attackStyle, target);
            CombatScript.delayHit(
                    npc,
                    2,
                    target,
                    attackStyle == NPCCombatDefinitions.RANGE ? getRangeHit(
                            npc, damage) : getMagicHit(npc, damage));
            if (defs.getAttackProjectile() != -1) {
                World.sendProjectile(npc, target, defs.getAttackProjectile(),
                        41, 16, 41, 35, 16, 0);
            }
        }
        if (defs.getAttackGfx() != -1) {
            npc.setNextGraphics(new Graphics(defs.getAttackGfx()));
        }
        npc.setNextAnimation(new Animation(defs.getAttackEmote()));
        return defs.getAttackDelay();
    }
}
