package com.rs.player.controlers.trollinvasion;

import com.rs.world.Entity;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

@SuppressWarnings("serial")
public class Cliff_Troll extends NPC {

	private final TrollInvasion controler;

	public Cliff_Troll(final int id, final WorldTile tile,
			final TrollInvasion controler) {
		super(id, tile, -1, true, true);
		this.controler = controler;
		setForceMultiArea(true);
		setNoDistanceCheck(true);
		setForceAgressive(true);
	}

	@Override
	public void sendDeath(final Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		controler.addKill();
		getCombat().removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
				} else if (loop >= defs.getDeathDelay()) {
					reset();
					finish();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

}
