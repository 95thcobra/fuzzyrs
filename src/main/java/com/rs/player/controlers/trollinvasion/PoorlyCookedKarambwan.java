package com.rs.player.controlers.trollinvasion;

import com.rs.utils.Utils;
import com.rs.entity.Entity;
import com.rs.world.ForceTalk;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class PoorlyCookedKarambwan extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Poorly cooked Karambwan", 13669 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.setForceAgressive(true);
		int hit = 0;
		final int attackStyle = Utils.random(2);
		switch (attackStyle) {
		case 0:
			target.setNextForceTalk(new ForceTalk("*cough*"));
			npc.setNextForceTalk(new ForceTalk("*erk*"));
			hit = CombatScript.getRandomMaxHit(npc, 200, NPCCombatDefinitions.MELEE, target);
			CombatScript.delayHit(npc, 1, target, CombatScript.getMeleeHit(npc, hit));
			break;
		case 1:
			hit = CombatScript.getRandomMaxHit(npc, 200, NPCCombatDefinitions.MELEE, target);
			CombatScript.delayHit(npc, 1, target, CombatScript.getMeleeHit(npc, hit));
			break;
		}
		return defs.getAttackDelay();
	}

}
