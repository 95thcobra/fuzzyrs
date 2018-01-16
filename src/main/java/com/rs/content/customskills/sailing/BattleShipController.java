package com.rs.content.customskills.sailing;

import com.rs.core.cores.CoresManager;
import com.rs.core.utils.Logger;
import com.rs.player.content.FadingScreen;
import com.rs.player.controlers.Controller;
import com.rs.world.RegionBuilder;
import com.rs.world.WorldTile;

/**
 * @author John (FuzzyAvacado) on 3/13/2016.
 */
public class BattleShipController extends Controller {

    private int[] regionBase;
    private WorldTile base;

    @Override
    public void start() {
        final long fadeTime = FadingScreen.fade(player);
        generateRegion();
        createShip(0);
        createShip(1);
        CoresManager.SLOW_EXECUTOR.execute(() -> {
            try {
                FadingScreen.unfade(player, fadeTime, () -> {
                    //TODO
                });
            } catch (Throwable t) {
                Logger.handle(t);
            }
        });
        player.setNextWorldTile(new WorldTile(base.getX(), base.getY(), 1));
    }

    private void generateRegion() {
        regionBase = RegionBuilder.findEmptyChunkBound(8, 8);
        base = new WorldTile(regionBase[0] << 3, regionBase[1] << 3, 1);
        RegionBuilder.copyAllPlanesMap(375, 312, regionBase[0], regionBase[1], 8, 8);
    }

    private void createShip(int i) {
        switch (i) {
            case 0:
                RegionBuilder.copyChunk(459, 368, 0, regionBase[0] + 4, regionBase[1] + 3, 0, 2);
                RegionBuilder.copyChunk(460, 368, 0, regionBase[0] + 3, regionBase[1] + 3, 0, 2);
                RegionBuilder.copyChunk(461, 368, 0, regionBase[0] + 2, regionBase[1] + 3, 0, 2);
                RegionBuilder.copyChunk(459, 368, 1, regionBase[0] + 4, regionBase[1] + 3, 1, 2);
                RegionBuilder.copyChunk(460, 368, 1, regionBase[0] + 3, regionBase[1] + 3, 1, 2);
                RegionBuilder.copyChunk(461, 368, 1, regionBase[0] + 2, regionBase[1] + 3, 1, 2);
                RegionBuilder.copyChunk(459, 368, 2, regionBase[0] + 4, regionBase[1] + 3, 2, 2);
                RegionBuilder.copyChunk(460, 368, 2, regionBase[0] + 3, regionBase[1] + 3, 2, 2);
                RegionBuilder.copyChunk(461, 368, 2, regionBase[0] + 2, regionBase[1] + 3, 2, 2);
                break;
            case 1:
                RegionBuilder.copyChunk(459, 368, 0, regionBase[0] + 2, regionBase[1] + 2, 0, 0);
                RegionBuilder.copyChunk(460, 368, 0, regionBase[0] + 3, regionBase[1] + 2, 0, 0);
                RegionBuilder.copyChunk(461, 368, 0, regionBase[0] + 4, regionBase[1] + 2, 0, 0);
                RegionBuilder.copyChunk(459, 368, 1, regionBase[0] + 2, regionBase[1] + 2, 1, 0);
                RegionBuilder.copyChunk(460, 368, 1, regionBase[0] + 3, regionBase[1] + 2, 1, 0);
                RegionBuilder.copyChunk(461, 368, 1, regionBase[0] + 4, regionBase[1] + 2, 1, 0);
                RegionBuilder.copyChunk(459, 368, 2, regionBase[0] + 2, regionBase[1] + 2, 2, 0);
                RegionBuilder.copyChunk(460, 368, 2, regionBase[0] + 3, regionBase[1] + 2, 2, 0);
                RegionBuilder.copyChunk(461, 368, 2, regionBase[0] + 4, regionBase[1] + 2, 2, 0);
                break;
        }
    }

}
