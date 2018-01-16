package com.rs.core.cores;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class CoresManager {

    public static final ScheduledExecutorService SLOW_EXECUTOR = Runtime.getRuntime().availableProcessors() >= 6 ? Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() >= 12 ? 4 : 2, new DefaultThreadFactory("Slow Pool", Thread.MIN_PRIORITY)) : Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("Slow Pool", Thread.MIN_PRIORITY));

    public static final ScheduledExecutorService FAST_EXECUTOR = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("FastTasks Thread", Thread.MIN_PRIORITY));

    public static void shutdown() {
        SLOW_EXECUTOR.shutdown();
        FAST_EXECUTOR.shutdown();
    }
}
