package com.rs.world.npc.combat.impl;

import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.npc.slayer.GanodermicBeast;

public class GanodermicCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{14696, 14697};
    }

    @Override
    public int attack(final NPC n, final Entity target) {
        final GanodermicBeast npc = (GanodermicBeast) n;
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final Player player = (Player) target;
        if (player.withinDistance(npc, 3)) {
            CombatScript.delayHit(
                    npc,
                    1,
                    target,
                    CombatScript.getMagicHit(
                            npc,
                            CombatScript.getRandomMaxHit(npc, 400,
                                    NPCCombatDefinitions.MAGE, target)));
            npc.setNextAnimation(new Animation(15470));
            npc.setNextGraphics(new Graphics(2034));
            World.sendProjectile(npc, target, 2034, 10, 18, 50, 50, 0, 0);
            return defs.getAttackDelay();
        } else {
            CombatScript.delayHit(
                    npc,
                    1,
                    target,
                    CombatScript.getMagicHit(
                            npc,
                            CombatScript.getRandomMaxHit(npc, 400,
                                    NPCCombatDefinitions.MAGE, target)));
            npc.setNextAnimation(new Animation(15470));
            npc.setNextGraphics(new Graphics(2034));
            World.sendProjectile(npc, target, 2034, 10, 18, 50, 50, 0, 0);
        }
        return defs.getAttackDelay();
    }

}
