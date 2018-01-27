package com.rs.world.npc.combat.impl;

import com.rs.utils.Utils;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.ForceTalk;
import com.rs.world.Graphics;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

public class SunfreetCombat extends CombatScript {

    @Override
    public Object[] getKeys() {

        return new Object[]{15222};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        int attackStyle = Utils.random(2);
        if (attackStyle == 1) { // range
            final int distanceX = target.getX() - npc.getX();
            final int distanceY = target.getY() - npc.getY();
            final int size = npc.getSize();
            if (distanceX > size || distanceX < -1 || distanceY > size
                    || distanceY < -1) {
                attackStyle = Utils.random(2); // set mage
            } else {
                npc.setNextAnimation(new Animation(defs.getAttackEmote()));
                CombatScript.delayHit(
                        npc,
                        1,
                        target,
                        getMeleeHit(
                                npc,
                                getRandomMaxHit(npc, defs.getMaxHit(),
                                        NPCCombatDefinitions.MELEE, target)));
                return defs.getAttackDelay();
            }
        }
        if (attackStyle == 2) { // arrow spray
            npc.setNextAnimation(new Animation(16318));
            npc.setNextForceTalk(new ForceTalk("FIRE ARCHER!"));
            WorldTasksManager.schedule(new WorldTask() {
                @Override
                public void run() {
                    target.setNextGraphics(new Graphics(439));
                    npc.setNextAnimation(new Animation(16317));
                    for (final Entity t : npc.getPossibleTargets()) {
                        if (!t.withinDistance(npc, 18)) {
                            continue;
                        }
                        final int damage = getRandomMaxHit(npc,
                                defs.getMaxHit(), NPCCombatDefinitions.RANGE, t);
                        if (damage > 0) {
                            CombatScript.delayHit(npc, 0, t, getMagicHit(npc, damage));
                            t.setNextGraphics(new Graphics(439, 0, 100));
                        }
                    }
                    CombatScript.delayHit(
                            npc,
                            1,
                            target,
                            getRangeHit(
                                    npc,
                                    getRandomMaxHit(npc, defs.getMaxHit() - 2,
                                            NPCCombatDefinitions.RANGE, target)));
                }
            }, 3);
        }
        if (attackStyle == 0) { // fire spray
            npc.setCapDamage(500);
            npc.setNextAnimation(new Animation(16318));
            npc.setNextForceTalk(new ForceTalk("FIRE SPRAY!"));
            WorldTasksManager.schedule(new WorldTask() {
                @Override
                public void run() {
                    WorldTasksManager.schedule(new WorldTask() {
                        @Override
                        public void run() {
                            target.setNextGraphics(new Graphics(3002, 0, 100));
                            npc.setNextAnimation(new Animation(16317));
                            for (final Entity t : npc.getPossibleTargets()) {
                                if (!t.withinDistance(npc, 18)) {
                                    continue;
                                }
                                final int damage = getRandomMaxHit(npc,
                                        defs.getMaxHit(),
                                        NPCCombatDefinitions.MAGE, t);
                                if (damage > 0) {
                                    CombatScript.delayHit(npc, 0, t,
                                            getMagicHit(npc, damage));
                                    t.setNextGraphics(new Graphics(3002, 0, 100));
                                }
                            }
                            CombatScript.delayHit(
                                    npc,
                                    0,
                                    target,
                                    getMagicHit(
                                            npc,
                                            getRandomMaxHit(npc,
                                                    defs.getMaxHit() - 2,
                                                    NPCCombatDefinitions.MAGE,
                                                    target)));
                        }

                    }, 1);
                }
            }, 2);
        }

        return defs.getAttackDelay();
    }

}