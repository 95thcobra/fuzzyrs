package com.rs.world.npc.combat.impl;

import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.Graphics;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class AbbysalTitanCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{7350, 7349};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        int damage = 0;
        damage = CombatScript.getRandomMaxHit(npc, 140, NPCCombatDefinitions.MELEE, target);
        npc.setNextAnimation(new Animation(7980));
        npc.setNextGraphics(new Graphics(1490));

        if (target instanceof Player) { // cjay failed dragonkk saved the day
            final Player player = (Player) target;
            if (damage > 0 && player.getPrayer().getPrayerpoints() > 0) {
                player.getPrayer().drainPrayer(damage / 2);
            }
        }
        CombatScript.delayHit(npc, 0, target, CombatScript.getMeleeHit(npc, damage));
        return defs.getAttackDelay();
    }
}
