package com.rs.player.content;

import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.ForceTalk;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

import java.util.List;

public class DonatorZone {

	public static void enterDonatorzone(final Player player) {
		Magic.sendNormalTeleportSpell(player, 0, 0,
				new WorldTile(2582, 3910, 0));
		for (final int regionId : player.getMapRegionsIds()) {
			final List<Integer> npcIndexes = World.getRegion(regionId)
					.getNPCsIndexes();
			if (npcIndexes != null) {
				for (final int npcIndex : npcIndexes) {
					final NPC n = World.getNPCs().get(npcIndex);
					if (n == null || n.getId() != 5445) {
						continue;
					}
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							final int random = Utils.getRandom(3);
							if (random == 0) {
								n.setNextForceTalk(new ForceTalk(
										"Everyone welcome "
												+ player.getDisplayName()
												+ " to the donator zone."));
							} else if (random == 1) {
								n.setNextForceTalk(new ForceTalk(player
										.getDisplayName()
										+ " has just joined the penguin zone."));
							} else if (random == 2) {
								n.setNextForceTalk(new ForceTalk("Ma boi "
										+ player.getDisplayName()
										+ " has just joined the penguin zone."));
							} else if (random == 3) {
								n.setNextForceTalk(new ForceTalk(
										"Who else wouldnt want "
												+ player.getDisplayName()
												+ " from joining the penguin zone."));
							}
						}
					}, 4);
				}
			}
		}
	}
}
