package com.rs.world.npc.kalph;

import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.ForceTalk;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

@SuppressWarnings("serial")
public class Kolo2 extends NPC {

    public Kolo2(final int id, final WorldTile tile, final int mapAreaNameHash,
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
                    if (getId() == 907) {
                        setCantInteract(true);
                        transformIntoNPC(11301);
                        setNextForceTalk(new ForceTalk(
                                "This is only the beginning, you can't beat me!"));
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
                        transformIntoNPC(11301);
                    }
                    stop();
                }
                loop++;
            }
        }, 0, 1);
    }

}
