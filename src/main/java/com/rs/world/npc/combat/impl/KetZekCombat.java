package com.rs.world.npc.combat.impl;

import com.rs.utils.Utils;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

public class KetZekCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{"Ket-Zek", 15207};
    }// anims: DeathEmote: 9257 DefEmote: 9253 AttackAnim: 9252 gfxs: healing:
    // 444 - healer

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final int distanceX = target.getX() - npc.getX();
        final int distanceY = target.getY() - npc.getY();
        final int size = npc.getSize();
        int hit = 0;
        if (distanceX > size || distanceX < -1 || distanceY > size
                || distanceY < -1) {
            commenceMagicAttack(npc, target, hit);
            return defs.getAttackDelay();
        }
        final int attackStyle = Utils.getRandom(1);
        switch (attackStyle) {
            case 0:
                hit = CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                        NPCCombatDefinitions.MELEE, target);
                npc.setNextAnimation(new Animation(defs.getAttackEmote()));
                CombatScript.delayHit(npc, 0, target, CombatScript.getMeleeHit(npc, hit));
                break;
            case 1:
                commenceMagicAttack(npc, target, hit);
                break;
        }
        return defs.getAttackDelay();
    }

    private void commenceMagicAttack(final NPC npc, final Entity target, int hit) {
        hit = CombatScript.getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit() - 50,
                NPCCombatDefinitions.MAGE, target);
        npc.setNextAnimation(new Animation(16136));
        // npc.setNextGraphics(new Graphics(1622, 0, 96 << 16));
        World.sendProjectile(npc, target, 2984, 34, 16, 30, 35, 16, 0);
        CombatScript.delayHit(npc, 2, target, CombatScript.getMagicHit(npc, hit));
        WorldTasksManager.schedule(new WorldTask() {

            @Override
            public void run() {
                target.setNextGraphics(new Graphics(2983, 0, 96 << 16));
            }
        }, 2);
    }
}
