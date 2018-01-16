package com.rs.player.content;

import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.concurrent.TimeUnit;

public final class FadingScreen {

    private FadingScreen() {

    }

    public static void fade(final Player player, final Runnable event) {
        unfade(player, fade(player), event);
    }

    public static void unfade(final Player player, final long startTime,
                              final Runnable event) {
        final long leftTime = 2500 - (Utils.currentTimeMillis() - startTime);
        if (leftTime > 0) {
            GameTaskManager.scheduleTask(new FadeTask(player, event, GameTask.ExecutionType.SCHEDULE, 0, leftTime, TimeUnit.MILLISECONDS));
        } else {
            unfade(player, event);
        }
    }

    public static void unfade(final Player player, final Runnable event) {
        event.run();
        WorldTasksManager.schedule(new WorldTask() {

            @Override
            public void run() {
                player.getInterfaceManager().sendFadingInterface(170);
                GameTaskManager.scheduleTask(new UnfadeTask(player, GameTask.ExecutionType.SCHEDULE, 1200, 0, TimeUnit.MILLISECONDS));
            }
        });
    }

    public static long fade(final Player player) {
        player.getInterfaceManager().sendFadingInterface(115);
        return Utils.currentTimeMillis();
    }

    @GameTaskType(GameTaskManager.GameTaskType.FAST)
    private static class FadeTask extends GameTask {

        private Player player;
        private Runnable r;

        public FadeTask(Player player, Runnable r, ExecutionType executionType, long initialDelay, long tick, TimeUnit timeUnit) {
            super(executionType, initialDelay, tick, timeUnit);
            this.player = player;
            this.r = r;
        }

        @Override
        public void run() {
            FadingScreen.unfade(player, r);
        }
    }

    @GameTaskType(GameTaskManager.GameTaskType.FAST)
    private static class UnfadeTask extends GameTask {

        private Player player;

        public UnfadeTask(Player player, ExecutionType executionType, long initialDelay, long tick, TimeUnit timeUnit) {
            super(executionType, initialDelay, tick, timeUnit);
            this.player = player;
        }

        @Override
        public void run() {
            player.getInterfaceManager().closeFadingInterface();
        }
    }
}
