package com.rs.world.npc.combat.impl;

import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

public class FakeNomadCombat extends CombatScript {

    @Override
    public Object[] getKeys() {

        return new Object[]{8529};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        npc.setNextAnimation(new Animation(12697));
        final boolean hit = CombatScript.getRandomMaxHit(npc, 50, NPCCombatDefinitions.MAGE,
                target) != 0;
        CombatScript.delayHit(npc, 2, target, CombatScript.getRegularHit(npc, hit ? 50 : 0));
        World.sendProjectile(npc, target, 1657, 30, 30, 75, 25, 0, 0);
        if (hit) {
            WorldTasksManager.schedule(new WorldTask() {
                @Override
                public void run() {
                    target.setNextGraphics(new Graphics(2278, 0, 100));
                }
            }, 1);
        }
        return defs.getAttackDelay();
    }

}
