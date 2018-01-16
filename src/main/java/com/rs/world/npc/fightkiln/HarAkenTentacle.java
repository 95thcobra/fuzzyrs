package com.rs.world.npc.fightkiln;

import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class HarAkenTentacle extends NPC {

    private final HarAken aken;

    public HarAkenTentacle(final int id, final WorldTile tile,
                           final HarAken aken) {
        super(id, tile, -1, true, true);
        setForceMultiArea(true);
        setCantFollowUnderCombat(true);
        setNextAnimation(new Animation(id == 15209 ? 16238 : 16241));
        this.aken = aken;
    }

    @Override
    public void sendDeath(final Entity source) {
        aken.removeTentacle(this);
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

    @Override
    public double getMagePrayerMultiplier() {
        return 0.1;
    }

    @Override
    public double getRangePrayerMultiplier() {
        return 0.1;
    }

    @Override
    public double getMeleePrayerMultiplier() {
        return 0.1;
    }
}
