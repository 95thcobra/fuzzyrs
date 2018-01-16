package com.rs.world.npc.combat.impl;

import com.rs.core.utils.Utils;
import com.rs.world.*;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class Kreearra extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{6222};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        if (!npc.isUnderCombat()) {
            npc.setNextAnimation(new Animation(17396));
            CombatScript.delayHit(
                    npc,
                    1,
                    target,
                    CombatScript.getMeleeHit(
                            npc,
                            CombatScript.getRandomMaxHit(npc, 260,
                                    NPCCombatDefinitions.MELEE, target)));
            return defs.getAttackDelay();
        }
        npc.setNextAnimation(new Animation(17397));
        for (final Entity t : npc.getPossibleTargets()) {
            if (Utils.getRandom(2) == 0) {
                sendMagicAttack(npc, t);
            } else {
                CombatScript.delayHit(
                        npc,
                        1,
                        t,
                        CombatScript.getRangeHit(
                                npc,
                                CombatScript.getRandomMaxHit(npc, 720,
                                        NPCCombatDefinitions.RANGE, t)));
                World.sendProjectile(npc, t, 1197, 41, 16, 41, 35, 16, 0);
                WorldTile teleTile = t;
                for (int trycount = 0; trycount < 10; trycount++) {
                    teleTile = new WorldTile(t, 2);
                    if (World.canMoveNPC(t.getPlane(), teleTile.getX(),
                            teleTile.getY(), t.getSize())) {
                        break;
                    }
                }
                t.setNextWorldTile(teleTile);
            }
        }
        return defs.getAttackDelay();
    }

    private void sendMagicAttack(final NPC npc, final Entity target) {
        npc.setNextAnimation(new Animation(17397));
        for (final Entity t : npc.getPossibleTargets()) {
            CombatScript.delayHit(
                    npc,
                    1,
                    t,
                    CombatScript.getMagicHit(
                            npc,
                            CombatScript.getRandomMaxHit(npc, 210,
                                    NPCCombatDefinitions.MAGE, t)));
            World.sendProjectile(npc, t, 1198, 41, 16, 41, 35, 16, 0);
            target.setNextGraphics(new Graphics(1196));
        }
    }
}
