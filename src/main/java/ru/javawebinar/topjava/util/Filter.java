package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalTime;

public class Filter {
    private final LocalTime timeFrom;
    private final LocalTime timeTo;

    private Filter(LocalTime timeFrom, LocalTime timeTo) {
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;

    }

    public LocalTime fromTime() {
        return timeFrom;
    }

    public LocalTime toTime() {
        return timeTo;
    }


    public boolean between(UserMeal meal) {
        LocalTime current = meal.getDateTime().toLocalTime();
        return current.compareTo(timeFrom) >= 0 && current.compareTo(timeTo) < 0;
    }

    public boolean between(UserMealWithExcess mealExcess) {
        LocalTime current = mealExcess.getDateTime().toLocalTime();
        return current.compareTo(timeFrom) >= 0 && current.compareTo(timeTo) < 0;
    }

    public static Filter of(LocalTime timeFrom, LocalTime timeTo) {
        return new Filter(timeFrom, timeTo);
    }
}
