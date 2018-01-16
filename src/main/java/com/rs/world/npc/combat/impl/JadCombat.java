package com.rs.world.npc.combat.impl;

import com.rs.core.utils.Utils;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

public class JadCombat extends CombatScript {

    @Override
    public Object[] getKeys() {

        return new Object[]{2745, 15208};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        int attackStyle = Utils.random(3);
        if (attackStyle == 2) { // melee
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
                        CombatScript.getMeleeHit(
                                npc,
                                CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                                        NPCCombatDefinitions.MELEE, target)));
                return defs.getAttackDelay();
            }
        }
        if (attackStyle == 1) { // range
            npc.setNextAnimation(new Animation(16202));
            npc.setNextGraphics(new Graphics(2994));
            WorldTasksManager.schedule(new WorldTask() {
                @Override
                public void run() {
                    target.setNextGraphics(new Graphics(3000));
                    CombatScript.delayHit(
                            npc,
                            1,
                            target,
                            CombatScript.getRangeHit(
                                    npc,
                                    CombatScript.getRandomMaxHit(npc, defs.getMaxHit() - 2,
                                            NPCCombatDefinitions.RANGE, target)));
                }
            }, 3);
        } else {
            npc.setNextAnimation(new Animation(16195));
            npc.setNextGraphics(new Graphics(2995));
            WorldTasksManager.schedule(new WorldTask() {
                @Override
                public void run() {
                    World.sendProjectile(npc, target, 2996, 80, 30, 40, 20, 5,
                            0);
                    WorldTasksManager.schedule(new WorldTask() {
                        @Override
                        public void run() {
                            target.setNextGraphics(new Graphics(2741, 0, 100));
                            CombatScript.delayHit(
                                    npc,
                                    0,
                                    target,
                                    CombatScript.getMagicHit(
                                            npc,
                                            CombatScript.getRandomMaxHit(npc,
                                                    defs.getMaxHit() - 2,
                                                    NPCCombatDefinitions.MAGE,
                                                    target)));
                        }

                    }, 1);
                }
            }, 2);
        }

        return defs.getAttackDelay() + 2;
    }

}
