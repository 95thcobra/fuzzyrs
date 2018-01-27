package com.rs.player.controlers.trollinvasion;

import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.Graphics;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class TrollRanger extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Troll ranger" };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final int damage = getRandomMaxHit(npc, 300,
				NPCCombatDefinitions.RANGE, target);
		npc.setNextGraphics(new Graphics(850));
		npc.setNextAnimation(new Animation(2134));
		// World.sendProjectile(npc, target, 32, 34, 16, 30, 35, 16, 0);// 32
		delayHit(npc, 2, target, getRangeHit(npc, damage));
		return defs.getAttackDelay();
	}

}
