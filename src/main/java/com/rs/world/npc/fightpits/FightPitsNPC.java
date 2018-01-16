package com.rs.world.npc.fightpits;

import com.rs.content.minigames.FightPits;
import com.rs.player.Player;
import com.rs.world.Entity;
import com.rs.world.Graphics;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class FightPitsNPC extends NPC {

    public FightPitsNPC(final int id, final WorldTile tile) {
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
        final ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
        for (final Player player : FightPits.arena) {
            possibleTarget.add(player);
        }
        return possibleTarget;
    }

}
