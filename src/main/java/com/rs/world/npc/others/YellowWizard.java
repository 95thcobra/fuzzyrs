package com.rs.world.npc.others;

import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.player.controlers.RunespanController;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

@SuppressWarnings("serial")
public class YellowWizard extends NPC {

    private final RunespanController controler;
    private final long spawnTime;

    public YellowWizard(final WorldTile tile, final RunespanController controler) {
        super(15430, tile, -1, true, true);
        spawnTime = Utils.currentTimeMillis();
        this.controler = controler;
    }

    public static void giveReward(final Player player) {

    }

    @Override
    public void processNPC() {
        if (spawnTime + 300000 < Utils.currentTimeMillis()) {
            finish();
        }
    }

    @Override
    public void finish() {
        controler.removeWizard();
        super.finish();
    }

    @Override
    public boolean withinDistance(final Player tile, final int distance) {
        return tile == controler.getPlayer()
                && super.withinDistance(tile, distance);
    }

}
