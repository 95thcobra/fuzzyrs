package com.rs.world.npc.kalph;

import com.rs.world.Animation;
import com.rs.world.Entity;
import com.rs.world.ForceTalk;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

@SuppressWarnings("serial")
public class Kolo3 extends NPC {

    public Kolo3(final int id, final WorldTile tile, final int mapAreaNameHash,
                 final boolean canBeAttackFromOutOfArea, final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
        setLureDelay(0);
        setForceAgressive(true);
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
                } else if (loop >= defs.getDeathDelay()) {
                    if (getId() == 11301) {
                        setCantInteract(true);
                        transformIntoNPC(11302);
                        setNextForceTalk(new ForceTalk(
                                "Foolish mortal, I am unstoppable."));
                        WorldTasksManager.schedule(new WorldTask() {

                            @Override
                            public void run() {
                                reset();
                                setCantInteract(false);
                            }

                        }, 5);
                    } else {
                        drop();
                        reset();
                        setLocation(getRespawnTile());
                        finish();
                        if (!isSpawned()) {
                            setRespawnTask();
                        }
                        transformIntoNPC(11302);
                    }
                    stop();
                }
                loop++;
            }
        }, 0, 1);
    }

}
