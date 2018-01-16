package com.rs.player.controlers.trollinvasion;

import com.rs.world.Entity;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class Dynamite extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Dynamite" };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int hit = 0;
		hit = getRandomMaxHit(npc, 200, NPCCombatDefinitions.MAGE, target);

		delayHit(npc, 2, target, getMagicHit(npc, hit));

		return defs.getAttackDelay();
	}

}
