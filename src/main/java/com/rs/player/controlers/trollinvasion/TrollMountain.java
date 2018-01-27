package com.rs.player.controlers.trollinvasion;

import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class TrollMountain extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Mountain troll" };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int hit = 0;
		hit = CombatScript.getRandomMaxHit(npc, 250, NPCCombatDefinitions.MELEE, target);
		npc.setNextAnimation(new Animation(13786));
		CombatScript.delayHit(npc, 2, target, CombatScript.getMeleeHit(npc, hit));
		return defs.getAttackDelay();
	}

}
