package com.rs.world.npc.others;

import com.rs.core.utils.Utils;
import com.rs.player.controlers.Barrows;
import com.rs.world.Entity;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public class BarrowsBrother extends NPC {

    private Barrows barrows;

    public BarrowsBrother(final int id, final WorldTile tile,
                          final Barrows barrows) {
        super(id, tile, -1, true, true);
        this.barrows = barrows;
    }

    @Override
    public void sendDeath(final Entity source) {
        if (barrows != null) {
            barrows.targetDied();
            barrows = null;
        }
        super.sendDeath(source);
    }

    @Override
    public double getMeleePrayerMultiplier() {
        return getId() != 2030 ? 0 : Utils.random(3) == 0 ? 1 : 0;
    }

    public void disapear() {
        barrows = null;
        finish();
    }

    @Override
    public void finish() {
        if (hasFinished())
            return;
        if (barrows != null) {
            barrows.targetFinishedWithoutDie();
            barrows = null;
        }
        super.finish();
    }

}
