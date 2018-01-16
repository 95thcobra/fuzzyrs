package com.rs.world.task.gametask.impl;

import com.rs.player.OwnedObjectManager;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.util.concurrent.TimeUnit;

/**
 * @author FuzzyAvacado
 */
@GameTaskType(GameTaskManager.GameTaskType.SLOW)
public class OwnedObjectsTask extends GameTask {

    public OwnedObjectsTask() {
        super(GameTask.ExecutionType.FIXED_DELAY, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        OwnedObjectManager.processAll();
    }
}
