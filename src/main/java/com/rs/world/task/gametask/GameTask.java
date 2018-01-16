package com.rs.world.task.gametask;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author FuzzyAvacado
 */
public abstract class GameTask implements Runnable {

    private final long initialDelay, tick;
    private final TimeUnit timeUnit;
    private volatile Future<?> future;
    private ExecutionType executionType;

    public GameTask(ExecutionType executionType, long initialDelay, long tick, TimeUnit timeUnit) {
        this.executionType = executionType;
        this.initialDelay = initialDelay;
        this.tick = tick;
        this.timeUnit = timeUnit;
    }

    public GameTask(ExecutionType executionType, long initialDelay, TimeUnit timeUnit) {
        this(executionType, initialDelay, 0, timeUnit);
    }

    public GameTask(ExecutionType executionType) {
        this(executionType, 0, 0, TimeUnit.MILLISECONDS);
    }

    protected void schedule(ScheduledExecutorService executorService) {
        switch (executionType) {
            case FIXED_DELAY:
                future = executorService.scheduleWithFixedDelay(this, initialDelay, tick, timeUnit);
                break;
            case FIXED_RATE:
                future = executorService.scheduleAtFixedRate(this, initialDelay, tick, timeUnit);
                break;
            case SUBMIT:
                future = executorService.submit(this);
                break;
            case SCHEDULE:
                future = executorService.schedule(this, initialDelay, timeUnit);
                break;
        }
    }

    public void cancel(boolean flag) {
        future.cancel(flag);
    }

    public final long getInitialDelay() {
        return initialDelay;
    }

    public final TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public final long getTick() {
        return tick;
    }

    public enum ExecutionType {
        FIXED_DELAY, FIXED_RATE, SUBMIT, SCHEDULE
    }
}
