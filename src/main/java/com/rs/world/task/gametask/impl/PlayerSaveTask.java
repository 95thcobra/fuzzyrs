package com.rs.world.task.gametask.impl;

import com.rs.server.Server;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.util.concurrent.TimeUnit;

/**
 * @author FuzzyAvacado
 */
@GameTaskType(GameTaskManager.GameTaskType.SLOW)
public class PlayerSaveTask extends GameTask {

    public PlayerSaveTask() {
        super(GameTask.ExecutionType.FIXED_DELAY, 5, 5, TimeUnit.MINUTES);
    }

    @Override
    public void run() {
        Server.getInstance().saveAll();
    }
}
