package com.rs.world.npc.fightkiln;

import com.rs.utils.Utils;
import com.rs.player.controlers.FightKiln;
import com.rs.entity.Entity;
import com.rs.world.Graphics;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class HarAken extends NPC {

    private final List<HarAkenTentacle> tentacles;
    private final FightKiln controler;
    private long time;
    private long spawnTentacleTime;
    private boolean underLava;

    public HarAken(final int id, final WorldTile tile, final FightKiln controler) {
        super(id, tile, -1, true, true);
        setForceMultiArea(true);
        this.controler = controler;
        tentacles = new ArrayList<HarAkenTentacle>();
    }

    public void resetTimer() {
        underLava = !underLava;
        if (time == 0) {
            spawnTentacleTime = Utils.currentTimeMillis() + 9000;
        }
        time = Utils.currentTimeMillis() + (underLava ? 45000 : 30000);
    }

    @Override
    public void sendDeath(final Entity source) {
        setNextGraphics(new Graphics(2924 + getSize()));
        if (time != 0) {
            removeTentacles();
            controler.removeNPC();
            time = 0;
        }
        super.sendDeath(source);
    }

    @Override
    public void processNPC() {
        if (isDead())
            return;
        cancelFaceEntityNoCheck();
    }

    public void process() {
        if (isDead())
            return;
        if (time != 0) {
            if (time < Utils.currentTimeMillis()) {
                if (underLava) {
                    controler.showHarAken();
                    resetTimer();
                } else {
                    controler.hideHarAken();
                }
            }
            if (spawnTentacleTime < Utils.currentTimeMillis()) {
                spawnTentacle();
            }

        }
    }

    public void spawnTentacle() {
        tentacles.add(new HarAkenTentacle(Utils.random(2) == 0 ? 15209 : 15210,
                controler.getTentacleTile(), this));
        spawnTentacleTime = Utils.currentTimeMillis()
                + Utils.random(15000, 25000);
    }

    public void removeTentacles() {
        for (final HarAkenTentacle t : tentacles) {
            t.finish();
        }
        tentacles.clear();
    }

    public void removeTentacle(final HarAkenTentacle tentacle) {
        tentacles.remove(tentacle);

    }

}
