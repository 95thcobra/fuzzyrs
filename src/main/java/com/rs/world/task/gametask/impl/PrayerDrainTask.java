package com.rs.world.task.gametask.impl;

import com.rs.player.Player;
import com.rs.world.World;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.util.concurrent.TimeUnit;

/**
 * @author FuzzyAvacado
 */
@GameTaskType(GameTaskManager.GameTaskType.FAST)
public class PrayerDrainTask extends GameTask {


    public PrayerDrainTask() {
        super(GameTask.ExecutionType.FIXED_DELAY, 0, 600, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        for (final Player player : World.getPlayers()) {
            if (player == null || player.isDead()
                    || !player.isRunning()) {
                continue;
            }
            player.getPrayer().processPrayerDrain();
        }
    }
}
