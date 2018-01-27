package com.rs.world.npc.zombies;

import com.rs.player.Player;
import com.rs.entity.Entity;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ZombieCaves extends NPC {

    public ZombieCaves(final int id, final WorldTile tile) {
        super(id, tile, -1, true, true);
        setForceMultiArea(true);
        setNoDistanceCheck(true);
    }

    @Override
    public void sendDeath(final Entity source) {
        setNextGraphics(new Graphics(2924 + getSize()));
        super.sendDeath(source);
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
