package com.rs.world.task.gametask.impl;

import com.rs.core.cores.ServerOnlineTime;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.util.concurrent.TimeUnit;

/**
 * @author John (FuzzyAvacado) on 12/20/2015.
 */
@GameTaskType(GameTaskManager.GameTaskType.FAST)
public class ServerOnlineUpdateTask extends GameTask {

    public ServerOnlineUpdateTask() {
        super(ExecutionType.FIXED_RATE, 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public void run() {
        ServerOnlineTime.increment();
    }
}
