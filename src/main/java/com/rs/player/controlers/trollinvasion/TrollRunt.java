package com.rs.player.controlers.trollinvasion;

import com.rs.world.Entity;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class TrollRunt extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Troll runt", 7361, 7362 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int hit = 0;
		hit = CombatScript.getRandomMaxHit(npc, 200, NPCCombatDefinitions.MELEE, target);
		CombatScript.delayHit(npc, 2, target, CombatScript.getMeleeHit(npc, hit));

		return defs.getAttackDelay();
	}

}
