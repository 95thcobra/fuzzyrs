package com.rs.world.npc.fightcaves;

import com.rs.player.controlers.FightCaves;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.Graphics;
import com.rs.world.WorldTile;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

@SuppressWarnings("serial")
public class TzTok_Jad extends FightCavesNPC {

    private final FightCaves controler;
    private boolean spawnedMinions;

    public TzTok_Jad(final int id, final WorldTile tile,
                     final FightCaves controler) {
        super(id, tile);
        this.controler = controler;
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (!spawnedMinions && getHitpoints() < getMaxHitpoints() / 2) {
            spawnedMinions = true;
            controler.spawnHealers();
        }
    }

    @Override
    public void sendDeath(final Entity source) {
        final NPCCombatDefinitions defs = getCombatDefinitions();
        resetWalkSteps();
        getCombat().removeTarget();
        setNextAnimation(null);
        WorldTasksManager.schedule(new WorldTask() {
            int loop;

            @Override
            public void run() {
                if (loop == 0) {
                    setNextAnimation(new Animation(defs.getDeathEmote()));
                    setNextGraphics(new Graphics(2924 + getSize()));
                } else if (loop >= defs.getDeathDelay()) {
                    reset();
                    finish();
                    controler.win();
                    stop();
                }
                loop++;
            }
        }, 0, 1);
    }

}
