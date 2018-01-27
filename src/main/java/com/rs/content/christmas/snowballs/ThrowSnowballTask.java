package com.rs.content.christmas.snowballs;

import com.rs.utils.Logger;
import com.rs.player.Player;
import com.rs.world.Graphics;
import com.rs.task.gametask.GameTask;
import com.rs.task.gametask.GameTaskManager;
import com.rs.task.gametask.GameTaskType;

import java.util.concurrent.TimeUnit;

/**
 * @author FuzzyAvacado
 */
@GameTaskType(GameTaskManager.GameTaskType.FAST)
public class ThrowSnowballTask extends GameTask {

    private int snowBallTime = 3;
    private Player target;

    public ThrowSnowballTask(Player target, int snowBallTime, ExecutionType executionType, long initialDelay, long tick, TimeUnit timeUnit) {
        super(executionType, initialDelay, tick, timeUnit);
        this.target = target;
        this.snowBallTime = snowBallTime;
    }

    @Override
    public void run() {
        try {
            if(snowBallTime ==1){
                target.setNextGraphics(new Graphics(1282));
            }
            if (snowBallTime <= 0) {
                cancel(true);
                return;
            }
            if (snowBallTime >= 0) {
                snowBallTime--;
            }
        } catch (Throwable e) {
            Logger.handle(e);
        }
    }
}
