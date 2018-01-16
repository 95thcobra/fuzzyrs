package com.rs.core.utils;

import java.util.Calendar;

/**
 * @author John (FuzzyAvacado) on 12/12/2015.
 */
public class DateUtils {

    public static boolean isWeekend() {
        return dayOfWeek() == 1 || dayOfWeek() == 6 || dayOfWeek() == 7;
    }

    public static int dayOfWeek() {
        final Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK);
    }
}
