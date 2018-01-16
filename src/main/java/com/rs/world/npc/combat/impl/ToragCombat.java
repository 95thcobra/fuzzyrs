package com.rs.world.npc.combat.impl;

import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.Graphics;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class ToragCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{2029};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        npc.setNextAnimation(new Animation(defs.getAttackEmote()));
        final int damage = CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                NPCCombatDefinitions.MELEE, target);
        if (damage != 0 && target instanceof Player && Utils.random(3) == 0) {
            target.setNextGraphics(new Graphics(399));
            final Player targetPlayer = (Player) target;
            targetPlayer
                    .setRunEnergy(targetPlayer.getRunEnergy() > 4 ? targetPlayer
                            .getRunEnergy() - 4 : 0);
        }
        CombatScript.delayHit(npc, 0, target, CombatScript.getMeleeHit(npc, damage));
        return defs.getAttackDelay();
    }
}
