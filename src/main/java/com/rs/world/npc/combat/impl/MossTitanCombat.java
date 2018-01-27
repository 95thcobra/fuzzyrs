package com.rs.world.npc.combat.impl;

import com.rs.utils.Utils;
import com.rs.player.controlers.Wilderness;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.npc.familiar.Familiar;

public class MossTitanCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{7330, 7329};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final Familiar familiar = (Familiar) npc;
        final boolean usingSpecial = familiar.hasSpecialOn();
        int damage = 0;
        if (usingSpecial) {// priority over regular attack
            npc.setNextAnimation(new Animation(8223));
            npc.setNextGraphics(new Graphics(1460));
            for (final Entity targets : npc.getPossibleTargets()) {
                if (targets.equals(target) && !targets.isAtMultiArea()) {
                    continue;
                }
                sendSpecialAttack(targets, npc);
            }
            sendSpecialAttack(target, npc);
        } else {
            damage = getRandomMaxHit(npc, 160, NPCCombatDefinitions.MELEE,
                    target);
            npc.setNextAnimation(new Animation(8222));
            CombatScript.delayHit(npc, 1, target, getMeleeHit(npc, damage));
        }
        return defs.getAttackDelay();
    }

    public void sendSpecialAttack(final Entity target, final NPC npc) {
        if (target.isAtMultiArea() && Wilderness.isAtWild(target)) {
            CombatScript.delayHit(
                    npc,
                    1,
                    target,
                    getMagicHit(
                            npc,
                            getRandomMaxHit(npc, 160,
                                    NPCCombatDefinitions.MAGE, target)));
            World.sendProjectile(npc, target, 1462, 34, 16, 30, 35, 16, 0);
            if (Utils.getRandom(3) == 0) {
                target.getPoison().makePoisoned(58);
            }
        }
    }
}
