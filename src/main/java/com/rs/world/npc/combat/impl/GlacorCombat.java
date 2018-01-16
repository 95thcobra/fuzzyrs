package com.rs.world.npc.combat.impl;

import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

/**
 * @author 'Corey 2010
 */

public class GlacorCombat extends CombatScript {

    public boolean spawnedMinions = false;
    public NPC Glacor;

    @Override
    public Object[] getKeys() {
        return (new Object[]{Integer.valueOf(14301)});
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        final int hit = Utils.getRandom(294);
        final int specialHit = Utils.getRandom(695);
        final int attackStyle = Utils.getRandom(3);
        final int distanceX = target.getX() - npc.getX();
        final int distanceY = target.getY() - npc.getY();
        final Player player = (Player) target;
        if (npc.getHitpoints() <= 2500 && !spawnedMinions) {
            // for you TODO Correctly spawn minions.
            spawnedMinions = true;
        }
        if (distanceX < -1 || distanceY < -1) {
            npc.setNextAnimation(new Animation(412));
            World.sendProjectile(npc, target, 1887, 34, 16, 40, 35, 16, 0);
            CombatScript.delayHit(npc, 1, target, getMagicHit(npc, specialHit));
        } else {
            switch (attackStyle) {
                case 2:
                    npc.setNextAnimation(new Animation(9955));
                    CombatScript.delayHit(npc, 0, target, getMeleeHit(npc, hit));
                    break;
                case 3:
                    npc.setNextAnimation(new Animation(412));
                    World.sendProjectile(npc, target, 1887, 34, 16, 40, 35, 16, 0);
                    CombatScript.delayHit(npc, 1, target,
                            getMagicHit(npc, specialHit));
                    break;
                case 0:
                    npc.setNextAnimation(new Animation(9968));
                    CombatScript.delayHit(npc, 1, target, getRangeHit(npc, hit));
                    break;
                case 1:
                    npc.setNextAnimation(new Animation(9968));
                    CombatScript.delayHit(npc, 1, target, getRangeHit(npc, hit));
                    break;
            }
        }
        return defs.getAttackDelay();
    }
}