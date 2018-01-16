package com.rs.world.npc.combat.impl;

import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.npc.familiar.Familiar;

public class MinotaurCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{"Bronze Minotaur", "Iron Minotaur",
                "Steel Minotaur", "Mithril Minotaur", "Adamant Minotaur",
                "Rune Minotaur"};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final Familiar familiar = (Familiar) npc;
        final boolean usingSpecial = familiar.hasSpecialOn();
        if (usingSpecial) {// priority over regular attack
            familiar.submitSpecial(familiar.getOwner());
            npc.setNextAnimation(new Animation(8026));
            npc.setNextGraphics(new Graphics(1334));
            World.sendProjectile(npc, target, 1333, 34, 16, 30, 35, 16, 0);
        } else {
            npc.setNextAnimation(new Animation(6829));
            CombatScript.delayHit(
                    npc,
                    1,
                    target,
                    CombatScript.getMagicHit(
                            npc,
                            CombatScript.getRandomMaxHit(npc, 40, NPCCombatDefinitions.MAGE,
                                    target)));
        }
        return defs.getAttackDelay();
    }
}
