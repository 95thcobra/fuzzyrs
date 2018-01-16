package com.rs.player.controlers.trollinvasion;

import com.rs.player.Player;
import com.rs.world.Entity;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class TrollInvasionNPC extends NPC {

	private final TrollInvasion controler;

	public TrollInvasionNPC(final int id, final WorldTile tile,
			final TrollInvasion controler) {
		super(id, tile, -1, true, true);
		this.controler = controler;
		setForceMultiArea(true);
		setNoDistanceCheck(true);
	}

	@Override
	public void sendDeath(final Entity source) {
		super.sendDeath(source);
		controler.addKill();
	}

	@Override
	public ArrayList<Entity> getPossibleTargets() {
		final ArrayList<Entity> possibleTarget = new ArrayList<Entity>(1);
		final List<Integer> playerIndexes = World.getRegion(getRegionId())
				.getPlayerIndexes();
		if (playerIndexes != null) {
			for (final int npcIndex : playerIndexes) {
				final Player player = World.getPlayers().get(npcIndex);
				if (player == null || player.isDead() || player.hasFinished()
						|| !player.isRunning()) {
					continue;
				}
				possibleTarget.add(player);
			}
		}
		return possibleTarget;
	}

}
