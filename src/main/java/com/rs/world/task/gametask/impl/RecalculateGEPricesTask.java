package com.rs.world.task.gametask.impl;

import com.rs.content.economy.exchange.GrandExchangePriceManager;
import com.rs.core.file.managers.GrandExchangeFileManager;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.util.concurrent.TimeUnit;

/**
 * Created by John on 12/10/2015.
 */
@GameTaskType(GameTaskManager.GameTaskType.SLOW)
public class RecalculateGEPricesTask extends GameTask {

    public RecalculateGEPricesTask() {
        super(ExecutionType.FIXED_RATE, 0, 12, TimeUnit.HOURS);
    }

    @Override
    public void run() {
        GrandExchangePriceManager.calculatePrices();
        GrandExchangeFileManager.saveGEPrices(GrandExchangePriceManager.getPrices());
    }
}
