package com.rs.world.task.gametask.impl;

import com.rs.player.Player;
import com.rs.world.World;
import com.rs.world.npc.NPC;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.util.concurrent.TimeUnit;

/**
 * @author FuzzyAvacado
 */
@GameTaskType(GameTaskManager.GameTaskType.FAST)
public class RestoreHitPointsTask extends GameTask {


    public RestoreHitPointsTask() {
        super(GameTask.ExecutionType.FIXED_DELAY, 0, 6, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        for (final Player player : World.getPlayers()) {
            if (player == null || player.isDead()
                    || !player.isRunning()) {
                continue;
            }
            player.restoreHitPoints();
        }
        for (final NPC npc : World.getNPCs()) {
            if (npc == null || npc.isDead() || npc.hasFinished()) {
                continue;
            }
            npc.restoreHitPoints();
        }
    }
}
