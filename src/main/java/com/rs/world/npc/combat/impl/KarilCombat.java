package com.rs.world.npc.combat.impl;

import com.rs.content.actions.skills.Skills;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class KarilCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{2028};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        npc.setNextAnimation(new Animation(defs.getAttackEmote()));
        final int damage = getRandomMaxHit(npc, defs.getMaxHit(),
                NPCCombatDefinitions.RANGE, target);
        if (damage != 0 && target instanceof Player && Utils.random(3) == 0) {
            target.setNextGraphics(new Graphics(401, 0, 100));
            final Player targetPlayer = (Player) target;
            final int drain = (int) (targetPlayer.getSkills().getLevelForXp(
                    Skills.AGILITY) * 0.2);
            final int currentLevel = targetPlayer.getSkills().getLevel(
                    Skills.AGILITY);
            targetPlayer.getSkills().set(Skills.AGILITY,
                    currentLevel < drain ? 0 : currentLevel - drain);
        }
        World.sendProjectile(npc, target, defs.getAttackProjectile(), 41, 16,
                41, 35, 16, 0);
        delayHit(npc, 2, target, getRangeHit(npc, damage));
        return defs.getAttackDelay();
    }
}
