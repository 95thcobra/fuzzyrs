package com.rs.world.npc.nomad;

import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.*;
import com.rs.world.npc.NPC;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.List;

@SuppressWarnings("serial")
public class FlameVortex extends NPC {

    private long explodeTime;

    public FlameVortex(final WorldTile tile) {
        this(9441, tile, -1, true, true);
    }

    public FlameVortex(final int id, final WorldTile tile,
                       final int mapAreaNameHash, final boolean canBeAttackFromOutOfArea,
                       final boolean spawned) {
        super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
        explodeTime = Utils.currentTimeMillis() + 60000;
        setNextAnimation(new Animation(12720));
    }

    @Override
    public void processNPC() {
        if (explodeTime == -1)
            return;
        final Player target = getTargetToCheck();
        if (target != null
                && ((target.getX() == getX() && target.getY() == getY()) || (target
                .getNextRunDirection() != -1
                && target.getX()
                - Utils.DIRECTION_DELTA_X[target
                .getNextRunDirection()] == getX() && target
                .getY()
                - Utils.DIRECTION_DELTA_Y[target.getNextRunDirection()] == getY()))) {
            explode(target, 400);
        } else if (explodeTime < Utils.currentTimeMillis()) {
            explode(target != null && withinDistance(target, 1) ? target : null,
                    Utils.random(400, 701));
        }
    }

    public void explode(final Player target, final int damage) {
        explodeTime = -1;
        final NPC npc = this;
        WorldTasksManager.schedule(new WorldTask() {

            private boolean secondLoop;

            @Override
            public void run() {
                if (!secondLoop) {
                    setNextAnimation(new Animation(12722));
                    if (target != null) {
                        target.applyHit(new Hit(npc, damage,
                                Hit.HitLook.REGULAR_DAMAGE));
                        target.setRunEnergy(0);
                        target.setNextForceTalk(new ForceTalk("Aiiiiiieeeee!"));
                    }
                    secondLoop = true;
                } else {
                    finish();
                    stop();
                }
            }
        }, 0, 0);
    }

    public Player getTargetToCheck() {
        final List<Integer> playerIndexes = World.getRegion(getRegionId())
                .getPlayerIndexes();
        if (playerIndexes != null) {
            for (final int npcIndex : playerIndexes) {
                final Player player = World.getPlayers().get(npcIndex);
                if (player == null || player.isDead() || !player.isRunning()) {
                    continue;
                }
                return player;
            }
        }
        return null;
    }

}
