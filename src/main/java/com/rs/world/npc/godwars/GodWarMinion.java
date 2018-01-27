package com.rs.world.npc.godwars;

import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.npc.NPC;
import com.rs.world.npc.combat.NPCCombatDefinitions;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class GodWarMinion extends NPC {

    public GodWarMinion(final int id, final WorldTile tile,
                        final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea,
                        final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
    }

    @Override
    public ArrayList<Entity> getPossibleTargets() {
        final ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
        for (final int regionId : getMapRegionsIds()) {
            final List<Integer> playerIndexes = World.getRegion(regionId)
                    .getPlayerIndexes();
            if (playerIndexes != null) {
                for (final int npcIndex : playerIndexes) {
                    final Player player = World.getPlayers().get(npcIndex);
                    if (player == null
                            || player.isDead()
                            || player.hasFinished()
                            || !player.isRunning()
                            || !player.withinDistance(this, 64)
                            || ((!isAtMultiArea() || !player.isAtMultiArea())
                            && player.getAttackedBy() != this && player
                            .getAttackedByDelay() > Utils
                            .currentTimeMillis())
                            || !clipedProjectile(player, false)) {
                        continue;
                    }
                    possibleTarget.add(player);
                }
            }
        }
        return possibleTarget;
    }

    /*
     * gotta override else setRespawnTask override doesnt work
     */
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
    }

    public void respawn() {
        setFinished(false);
        World.addNPC(this);
        setLastRegionId(0);
        World.updateEntityRegion(this);
        loadMapRegions();
        checkMultiArea();
    }

}
