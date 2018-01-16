package com.rs.world.npc.combat.impl;

import com.rs.core.utils.Utils;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class TokXilCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{"Tok-Xil", 15205};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final int distanceX = target.getX() - npc.getX();
        final int distanceY = target.getY() - npc.getY();
        final int size = npc.getSize();
        int hit = 0;
        int attackStyle = Utils.random(2);
        if (attackStyle == 0
                && (distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1)) {
            attackStyle = 1;
        }
        switch (attackStyle) {
            case 0:
                hit = getRandomMaxHit(npc, defs.getMaxHit() - 36,
                        NPCCombatDefinitions.MELEE, target);
                npc.setNextAnimation(new Animation(defs.getAttackEmote()));
                delayHit(npc, 0, target, getMeleeHit(npc, hit));
                break;
            case 1:
                hit = getRandomMaxHit(npc, defs.getMaxHit(),
                        NPCCombatDefinitions.RANGE, target);
                npc.setNextAnimation(new Animation(16132));
                World.sendProjectile(npc, target, 2993, 34, 16, 30, 35, 16, 0);
                delayHit(npc, 2, target, getRangeHit(npc, hit));
                break;
        }
        return defs.getAttackDelay();
    }
}
