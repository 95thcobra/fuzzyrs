package com.rs.core.cores;

/**
 * @author John (FuzzyAvacado) on 12/20/2015.
 */
public class ServerOnlineTime {

    private static long time = 0;

    public static void increment() {
        time++;
    }

    public static long getHours() {
        return time / 60;
    }

    public static long getDays() {
        return getHours() / 24;
    }

    public static long getMinutes() {
        return time % 60;
    }

    public static long getTime() {
        return time;
    }

    public static void setTime(int newTime) {
        time = newTime;
    }

}
