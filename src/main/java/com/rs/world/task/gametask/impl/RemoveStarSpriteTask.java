package com.rs.world.task.gametask.impl;

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
public class RemoveStarSpriteTask extends GameTask {

    public RemoveStarSpriteTask() {
        super(ExecutionType.FIXED_RATE, 60, 50 * 600, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        for (final NPC n : World.getNPCs()) {
            if (n == null || n.getId() != 8091) {
                continue;
            }
            n.sendDeath(n);
        }
    }
}
