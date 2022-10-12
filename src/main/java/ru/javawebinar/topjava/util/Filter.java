package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealExcess;

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


    public boolean between(Meal meal) {
        LocalTime current = meal.getDateTime().toLocalTime();
        return current.compareTo(timeFrom) >= 0 && current.compareTo(timeTo) < 0;
    }

    public boolean between(MealExcess mealExcess) {
        LocalTime current = mealExcess.getDateTime().toLocalTime();
        return current.compareTo(timeFrom) >= 0 && current.compareTo(timeTo) < 0;
    }

    public static Filter of(LocalTime timeFrom, LocalTime timeTo) {
        return new Filter(timeFrom, timeTo);
    }
}
