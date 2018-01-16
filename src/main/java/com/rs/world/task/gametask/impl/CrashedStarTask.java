package com.rs.world.task.gametask.impl;

import com.rs.player.content.ShootingStar;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.util.concurrent.TimeUnit;

/**
 * @author FuzzyAvacado
 */
@GameTaskType(GameTaskManager.GameTaskType.FAST)
public class CrashedStarTask extends GameTask {

    public CrashedStarTask() {
        super(ExecutionType.FIXED_DELAY, 0, 1200 * 600, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        ShootingStar.spawnRandomStar();
    }
}
