package com.rs.world.npc.others;

import com.rs.core.cores.CoresManager;
import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("serial")
public class Glacor extends NPC {

    public Glacor(final int id, final WorldTile tile,
                  final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea,
                  final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
        setLureDelay(0);
        setRun(true);
        setForceMultiAttacked(true);
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
                    setNextAnimation(new Animation(9961));
                } else if (loop >= defs.getDeathDelay()) {
                    drop();
                    reset();
                    setLocation(getRespawnTile());
                    finish();
                    setRespawnTask();
                    stop();
                }
                loop++;
            }
        }, 0, 1);
    }

    @Override
    public void setRespawnTask() {
        if (!hasFinished()) {
            reset();
            setLocation(getRespawnTile());
            finish();
        }
        final NPC npc = this;
        CoresManager.SLOW_EXECUTOR.schedule(new Runnable() {
                                               @Override
                                               public void run() {
                                                   setFinished(false);
                                                   World.addNPC(npc);
                                                   npc.setLastRegionId(0);
                                                   World.updateEntityRegion(npc);
                                                   loadMapRegions();
                                                   checkMultiArea();
                                               }
                                           }, getCombatDefinitions().getRespawnDelay() * 600,
                TimeUnit.MILLISECONDS);
    }

}