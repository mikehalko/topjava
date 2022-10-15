package ru.javawebinar.topjava.util;


// временно
public class CaloriesPerDay {
    private static int limit = 2000;
    public static int limit() {
        return limit;
    }

    public static void setLimit(int limit) {
        CaloriesPerDay.limit = limit;
    }
}
