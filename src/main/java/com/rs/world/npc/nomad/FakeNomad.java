package com.rs.world.npc.nomad;

import com.rs.world.Hit;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public class FakeNomad extends NPC {

    private final Nomad nomad;

    public FakeNomad(final WorldTile tile, final Nomad nomad) {
        super(8529, tile, -1, true, true);
        this.nomad = nomad;
        setForceMultiArea(true);
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        nomad.destroyCopy(this);
    }

}
