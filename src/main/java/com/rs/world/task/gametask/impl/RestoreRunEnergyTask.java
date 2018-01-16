package com.rs.world.task.gametask.impl;

import com.rs.content.actions.skills.Skills;
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
public class RestoreRunEnergyTask extends GameTask {

    public RestoreRunEnergyTask() {
        super(GameTask.ExecutionType.FIXED_DELAY, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        for (final Player player : World.getPlayers()) {
            if (player == null || player.isDead() || !player.isRunning() || (World.getCheckAgility() && player.getSkills().getLevel(Skills.AGILITY) < 70)) {
                continue;
            }
            player.restoreRunEnergy();
        }
        World.setCheckAgility(!World.getCheckAgility());
    }
}
