package com.rs.content.drinking;

import com.rs.player.Player;
import com.rs.world.task.gametask.GameTask;

import java.util.concurrent.TimeUnit;

/**
 * @author John (FuzzyAvacado) on 1/8/2016.
 */
public class DrunkGameTask extends GameTask {

    private final Player player;

    public DrunkGameTask(Player player) {
        super(GameTask.ExecutionType.SCHEDULE, 10, TimeUnit.MINUTES);
        this.player = player;
    }

    @Override
    public void run() {
        player.setAlcoholIntake(0);
        player.getAppearance().setRenderEmote(-1);
        this.cancel(true);
    }
}
