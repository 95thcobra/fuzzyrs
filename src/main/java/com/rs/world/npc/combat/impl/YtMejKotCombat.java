package com.rs.world.npc.combat.impl;

import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

import java.util.List;

public class YtMejKotCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{"Yt-MejKot"};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        npc.setNextAnimation(new Animation(defs.getAttackEmote()));
        CombatScript.delayHit(
                npc,
                0,
                target,
                CombatScript.getMeleeHit(
                        npc,
                        CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                                defs.getAttackStyle(), target)));
        if (npc.getHitpoints() < npc.getMaxHitpoints() / 2) {
            if (npc.getTemporaryAttributtes().remove("Heal") != null) {
                npc.setNextGraphics(new Graphics(2980, 0, 100));
                final List<Integer> npcIndexes = World.getRegion(
                        npc.getRegionId()).getNPCsIndexes();
                if (npcIndexes != null) {
                    for (final int npcIndex : npcIndexes) {
                        final NPC n = World.getNPCs().get(npcIndex);
                        if (n == null || n.isDead() || n.hasFinished()) {
                            continue;
                        }
                        n.heal(100);
                    }
                }
            } else {
                npc.getTemporaryAttributtes().put("Heal", Boolean.TRUE);
            }
        }
        return defs.getAttackDelay();
    }
}
