package com.rs.world.task.gametask.impl;

import com.rs.player.Player;
import com.rs.world.Graphics;
import com.rs.world.World;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.util.concurrent.TimeUnit;

/**
 * @author FuzzyAvacado
 */
@GameTaskType(GameTaskManager.GameTaskType.SLOW)
public class SummoningEffectTask extends GameTask {

    public SummoningEffectTask() {
        super(GameTask.ExecutionType.FIXED_DELAY, 0, 15, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        for (final Player player : World.getPlayers()) {
            if (player == null || player.getFamiliar() == null
                    || player.isDead() || !player.hasFinished()) {
                continue;
            }
            if (player.getFamiliar().getOriginalId() == 6814) {
                player.heal(20);
                player.setNextGraphics(new Graphics(1507));
            }
        }
    }
}
