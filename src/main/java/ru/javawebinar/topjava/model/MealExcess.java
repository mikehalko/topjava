package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class MealExcess {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private boolean excess;

    public MealExcess(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public static MealExcess of(Meal meal, boolean excess) {
        return new MealExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    @Override
    public String toString() {
        return "[UserMealWithExcess]:{" +
                "date-time=" + dateTime +
                ", desc='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }

    public void setExcess(boolean excess) {
        this.excess = excess;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
