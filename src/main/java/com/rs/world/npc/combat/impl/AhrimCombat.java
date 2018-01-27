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

public class AhrimCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{2025};
    }

    @Override
    public int attack(final NPC npc, final Entity target) {
        final NPCCombatDefinitions defs = npc.getCombatDefinitions();
        npc.setNextAnimation(new Animation(defs.getAttackEmote()));
        final int damage = CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
                NPCCombatDefinitions.MAGE, target);
        if (damage != 0 && target instanceof Player && Utils.random(3) == 0) {
            target.setNextGraphics(new Graphics(400, 0, 100));
            final Player targetPlayer = (Player) target;
            final int currentLevel = targetPlayer.getSkills().getLevel(
                    Skills.STRENGTH);
            targetPlayer.getSkills().set(Skills.STRENGTH,
                    currentLevel < 5 ? 0 : currentLevel - 5);
        }
        World.sendProjectile(npc, target, defs.getAttackProjectile(), 41, 16,
                41, 35, 16, 0);
        npc.setNextGraphics(new Graphics(defs.getAttackGfx()));
        CombatScript.delayHit(npc, 2, target, CombatScript.getMagicHit(npc, damage));
        return defs.getAttackDelay();
    }
}
