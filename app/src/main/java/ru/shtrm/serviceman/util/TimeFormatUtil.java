package ru.shtrm.serviceman.util;

import android.support.annotation.Nullable;

public class TimeFormatUtil {

    public static String formatTimeIntToString(int hour, int minute) {
        StringBuilder buffer = new StringBuilder(16);
        if (hour <= 9) {
            buffer.append("0");
        }
        buffer.append(hour).append(":");
        if (minute <= 9) {
            buffer.append("0");
        }
        buffer.append(minute);

        return buffer.toString();
    }

    @Nullable
    public static int[] formatTimeStringToIntArray(String timeString) {
        try {
            String[] times = timeString.split(":");
            int[] timeInts = new int[4];
            for (int i = 0; i < timeInts.length; i++) {
                timeInts[i] = Integer.parseInt(times[i]);
            }
            return timeInts;
        } catch (Exception ex) {
            return null;
        }
    }

}
