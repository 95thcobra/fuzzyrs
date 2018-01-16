package com.rs.player.controlers.trollinvasion;

import com.rs.core.utils.Utils;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class TrollGeneral extends CombatScript {

	@Override
	public Object[] getKeys() {

		return new Object[] { "Troll general", 12291 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int hit = 0;
		npc.setNextAnimation(new Animation(13788));

		hit = CombatScript.getRandomMaxHit(npc, Utils.random(300, 400),
				NPCCombatDefinitions.MELEE, target);
		CombatScript.delayHit(npc, 1, target, CombatScript.getMeleeHit(npc, hit));
		return defs.getAttackDelay();
	}

}
