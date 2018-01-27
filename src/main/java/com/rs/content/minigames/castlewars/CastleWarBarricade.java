package com.rs.content.minigames.castlewars;

import com.rs.utils.Utils;
import com.rs.entity.Entity;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public class CastleWarBarricade extends NPC {

	private final int team;

	public CastleWarBarricade(final int team, final WorldTile tile) {
		super(1532, tile, -1, true, true);
		setCantFollowUnderCombat(true);
		this.team = team;
	}

	@Override
	public void processNPC() {
		if (isDead())
			return;
		cancelFaceEntityNoCheck();
		if (getId() == 1533 && Utils.getRandom(20) == 0) {
			sendDeath(this);
		}
	}

	public void litFire() {
		transformIntoNPC(1533);
		sendDeath(this);
	}

	public void explode() {
		// TODO gfx
		sendDeath(this);
	}

	@Override
	public void sendDeath(final Entity killer) {
		resetWalkSteps();
		getCombat().removeTarget();
		if (this.getId() != 1533) {
			setNextAnimation(null);
			reset();
			setLocation(getRespawnTile());
			finish();
		} else {
			super.sendDeath(killer);
		}
		CastleWars.removeBarricade(team, this);
	}

}
