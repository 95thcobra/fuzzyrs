package com.rs.world.npc.combat.impl;

import com.rs.utils.Utils;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class HarAkenTentacleCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{15209, 15210};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final int distanceX = target.getX() - npc.getX();
        final int distanceY = target.getY() - npc.getY();
        final int size = npc.getSize();
        int attackStyle = Utils.random(2);
        if (attackStyle == 0
                && (distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1)) {
            attackStyle = 1;
        }
        switch (attackStyle) {
            case 0:
                npc.setNextAnimation(new Animation(defs.getAttackEmote()));
                CombatScript.delayHit(
                        npc,
                        0,
                        target,
                        CombatScript.getMeleeHit(
                                npc,
                                CombatScript.getRandomMaxHit(npc, defs.getMaxHit() - 36,
                                        NPCCombatDefinitions.MELEE, target)));
                break;
            case 1:
                npc.setNextAnimation(new Animation(npc.getId() == 15209 ? 16253
                        : 16242));
                World.sendProjectile(npc, target, npc.getId() == 15209 ? 3004
                        : 2922, 140, 35, 80, 35, 16, 0);
                if (npc.getId() == 15209) {
                    CombatScript.delayHit(
                            npc,
                            2,
                            target,
                            CombatScript.getRangeHit(
                                    npc,
                                    CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                                            NPCCombatDefinitions.RANGE, target)));
                } else {
                    CombatScript.delayHit(
                            npc,
                            2,
                            target,
                            CombatScript.getMagicHit(
                                    npc,
                                    CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                                            NPCCombatDefinitions.MAGE, target)));
                }
                break;
        }
        return defs.getAttackDelay();
    }
}
