package com.ugnavigate.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static String convertTo12HourFormat(String time) {
        LocalTime localTime = LocalTime.parse(time, TIME_FORMATTER);
        return localTime.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    public static String convertTo24HourFormat(String time) {
        LocalTime localTime = LocalTime.parse(time);
        return localTime.format(TIME_FORMATTER);
    }

    public static long estimateTravelTime(double distance, double speed) {
        if (speed <= 0) {
            throw new IllegalArgumentException("Speed must be greater than zero.");
        }
        return Math.round((distance / speed) * 60); // returns time in minutes
    }
}