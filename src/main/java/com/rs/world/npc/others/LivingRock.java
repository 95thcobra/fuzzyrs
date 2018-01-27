package com.rs.world.npc.others;

import com.rs.core.cores.CoresManager;
import com.rs.utils.Logger;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("serial")
public class LivingRock extends NPC {

    private Entity source;
    private long deathTime;

    public LivingRock(final int id, final WorldTile tile,
                      final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea,
                      final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
        setForceTargetDistance(4);
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
                    drop();
                    reset();
                    transformIntoRemains(source);
                    stop();
                }
                loop++;
            }
        }, 0, 1);
    }

    public void transformIntoRemains(final Entity source) {
        this.source = source;
        deathTime = Utils.currentTimeMillis();
        final int remainsId = getId() + 5;
        transformIntoNPC(remainsId);
        setRandomWalk(false);
        CoresManager.SLOW_EXECUTOR.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    if (remainsId == getId()) {
                        takeRemains();
                    }
                } catch (final Throwable e) {
                    Logger.handle(e);
                }
            }
        }, 3, TimeUnit.MINUTES);

    }

    public boolean canMine(final Player player) {
        return Utils.currentTimeMillis() - deathTime > 60000
                || player == source;
    }

    public void takeRemains() {
        setNPC(getId() - 5);
        setLocation(getRespawnTile());
        setRandomWalk(true);
        finish();
        if (!isSpawned()) {
            setRespawnTask();
        }
    }

}
