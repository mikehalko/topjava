package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class MealTo {
    private Integer id;

    private LocalDateTime dateTime;

    private String description;

    private Integer calories;

    private Boolean excess;

    public MealTo(LocalDateTime dateTime, String description, Integer calories, Boolean excess) {
        this.id = -1;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }
    public MealTo(Integer id, LocalDateTime dateTime, String description, Integer calories, Boolean excess) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Boolean isExcess() {
        return excess;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public void setExcess(Boolean excess) {
        this.excess = excess;
    }

    @Override
    public String toString() {
        return "[meal_exc]["+id+"]["+description+"]["+excess+"]["+dateTime+"]";
    }
}
