package com.rs.player.controlers.trollinvasion;

import com.rs.utils.Utils;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.Graphics;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.CombatScript;
import com.rs.world.npc.combat.NPCCombatDefinitions;

public class CliffCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Cliff", 13381 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.getRandom(2) == 0) { // range
			npc.setNextAnimation(new Animation(1933));
			CombatScript.delayHit(
					npc,
					1,
					target,
					CombatScript.getRangeHit(
							npc,
							CombatScript.getRandomMaxHit(npc, 335,
									NPCCombatDefinitions.RANGE, target)));
			target.setNextGraphics(new Graphics(755));
		} else { // melee attack
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			CombatScript.delayHit(
					npc,
					0,
					target,
					CombatScript.getMeleeHit(
							npc,
							CombatScript.getRandomMaxHit(npc, defs.getMaxHit(),
									NPCCombatDefinitions.MELEE, target)));
		}
		return defs.getAttackDelay();
	}
}
