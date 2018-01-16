package com.rs.world.npc.combat.impl;

import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.Graphics;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.npc.familiar.Familiar;

public class LavaTitanCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{7342, 7341};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final Familiar familiar = (Familiar) npc;
        final boolean usingSpecial = familiar.hasSpecialOn();
        int damage = 0;
        if (usingSpecial) {// priority over regular attack
            npc.setNextAnimation(new Animation(7883));
            npc.setNextGraphics(new Graphics(1491));
            CombatScript.delayHit(
                    npc,
                    1,
                    target,
                    CombatScript.getMeleeHit(
                            npc,
                            CombatScript.getRandomMaxHit(npc, 140,
                                    NPCCombatDefinitions.MELEE, target)));
            if (damage <= 4 && target instanceof Player) {
                final Player player = (Player) target;
                player.getCombatDefinitions().desecreaseSpecialAttack(
                        (player.getCombatDefinitions()
                                .getSpecialAttackPercentage() / 10));
            }
        } else {
            damage = CombatScript.getRandomMaxHit(npc, 140, NPCCombatDefinitions.MELEE,
                    target);
            npc.setNextAnimation(new Animation(7980));
            npc.setNextGraphics(new Graphics(1490));
            CombatScript.delayHit(npc, 1, target, CombatScript.getMeleeHit(npc, damage));
        }
        if (Utils.getRandom(10) == 0) {
            CombatScript.delayHit(npc, 1, target, CombatScript.getMeleeHit(npc, Utils.getRandom(50)));
        }
        return defs.getAttackDelay();
    }
}
