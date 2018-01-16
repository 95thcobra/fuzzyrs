package com.rs.world.task.gametask;

import com.rs.core.cores.CoresManager;
import com.rs.world.task.gametask.impl.*;
import com.sun.istack.internal.NotNull;

import java.lang.annotation.IncompleteAnnotationException;

/**
 * @author FuzzyAvacado
 */
public class GameTaskManager {

    public static void scheduleTask(@NotNull GameTask gameTask) {
        GameTaskType gameTaskType = parse(gameTask);
        if (gameTaskType != null) {
            gameTask.schedule(gameTaskType == GameTaskType.SLOW ? CoresManager.SLOW_EXECUTOR : CoresManager.FAST_EXECUTOR);
        }
    }

    public static void scheduleTask(@NotNull GameTask gameTask, GameTaskType gameTaskType) {
        gameTask.schedule(gameTaskType == GameTaskType.SLOW ? CoresManager.SLOW_EXECUTOR : CoresManager.FAST_EXECUTOR);
    }

    private static GameTaskType parse(GameTask gameTask) throws IncompleteAnnotationException {
        if (gameTask.getClass().isAnnotationPresent(com.rs.world.task.gametask.GameTaskType.class)) {
            return gameTask.getClass().getAnnotation(com.rs.world.task.gametask.GameTaskType.class).value();
        }
        return GameTaskType.FAST;

    }

    public static void init() {
        //Game tasks
        scheduleTask(new OwnedObjectsTask());
        scheduleTask(new QuestTabUpdateTask());
        scheduleTask(new RestoreShopItemsTask());
        scheduleTask(new SummoningEffectTask());
        scheduleTask(new RestoreSpecialAttackTask());
        scheduleTask(new RestoreRunEnergyTask());
        scheduleTask(new PrayerDrainTask());
        scheduleTask(new RestoreHitPointsTask());
        scheduleTask(new RestoreSkillsTask());
        scheduleTask(new ServerNewsTask());
        //Server tasks
        scheduleTask(new PlayerSaveTask());
        scheduleTask(new CleanMemoryTask());
        scheduleTask(new RecalculateGEPricesTask());
        //scheduleTask(new UpdatePlayersOnlineTask());

    }

    public enum GameTaskType {
        FAST, SLOW
    }

}
