package com.rs.core.utils;

import com.rs.server.Server;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.Calendar;

public final class Logger {

    private Logger() {
    }

    public static void handle(final Throwable throwable) {
        String message = Calendar.getInstance().getTime().toString() + " - ERROR! THREAD NAME: " + Thread.currentThread().getName() + " [" + throwable.getClass().getSimpleName() + "]";
        System.out.println(message);
        throwable.printStackTrace();
    }

    public static void debug(final long processTime) {
        info(Logger.class, "---DEBUG--- start");
        info(Logger.class, "WorldProcessTime: " + processTime);
        info(Logger.class,
                "WorldRunningTasks: " + WorldTasksManager.getTasksCount());
        info(Logger.class,
                "ConnectedChannels: "
                        + Server.getInstance().getNetworkEngine().getConnectedChannelsSize());
        info(Logger.class, "---DEBUG--- end");
    }

    public static void info(final Object classInstance, final Object message) {
        info(classInstance.getClass().getSimpleName(), message);
    }

    public static void info(final Class klass, final Object message) {
        info(klass.getSimpleName(), message);
    }

    public static void info(final String className, final Object message) {
        final String text = "[" + className + "]" + " " + message.toString();
        System.out.println(text);
    }

}
