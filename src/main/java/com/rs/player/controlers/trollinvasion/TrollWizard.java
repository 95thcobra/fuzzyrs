package com.rs.player.controlers.trollinvasion;

import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class TrollWizard extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 12435 };

	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int hit = 0;
		npc.setNextAnimation(new Animation(1938));
		hit = CombatScript.getRandomMaxHit(npc, 300, NPCCombatDefinitions.MELEE, target);
		CombatScript.delayHit(npc, 2, target, CombatScript.getMeleeHit(npc, hit));
		return defs.getAttackDelay();
	}

}
