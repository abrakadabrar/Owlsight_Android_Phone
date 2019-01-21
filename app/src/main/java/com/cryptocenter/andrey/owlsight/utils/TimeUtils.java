package com.cryptocenter.andrey.owlsight.utils;

public class TimeUtils {

    public static int secondsFromHours(String str) {
        str = str.substring(10);
        str = str.replaceAll("[-+.^:,]", "");
        int x1 = Integer.parseInt(str.substring(1, 2));
        int x2 = Integer.parseInt(str.substring(2, 3));
        int x3 = Integer.parseInt(str.substring(3, 4));
        int x4 = Integer.parseInt(str.substring(4, 5));
        int x5 = Integer.parseInt(str.substring(5, 6));
        int x6 = Integer.parseInt(str.substring(6, 7));
        int val1 = x1 * 10 * 60 * 60;
        int val2 = x2 * 60 * 60;
        int val3 = x3 * 10 * 60;
        int val4 = x4 * 60;
        int val5 = x5 * 10;
        int val6 = val5 + x6;
        return val1 + val2 + val3 + val4 + val6;
    }

    public static String timeConv(int totalSeconds) {
        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;
        final int seconds = totalSeconds % SECONDS_IN_A_MINUTE;
        final int totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
        final int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        final int hours = totalMinutes / MINUTES_IN_AN_HOUR;

        return (hours < 10 ? "0" + hours : hours) + " : " +  (minutes < 10 ? "0" + minutes : minutes)  + " : " + (seconds < 10 ? "0" + seconds : seconds) + " ";
    }
}
