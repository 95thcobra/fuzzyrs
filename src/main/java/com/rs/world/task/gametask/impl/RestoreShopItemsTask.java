package com.rs.world.task.gametask.impl;

import com.rs.content.economy.shops.ShopsManager;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.util.concurrent.TimeUnit;

/**
 * @author FuzzyAvacado
 */
@GameTaskType(GameTaskManager.GameTaskType.SLOW)
public class RestoreShopItemsTask extends GameTask {


    public RestoreShopItemsTask() {
        super(GameTask.ExecutionType.FIXED_DELAY, 0, 30, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        ShopsManager.restoreShops();
    }
}
