package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.util.LinkedList;
import java.util.List;

public class Pool {
    private int calories;
    private final List<UserMealWithExcess> meals;

    public Pool(int calories, List<UserMealWithExcess> meals) {
        this.calories = calories;
        this.meals = meals;
    }

    public static Pool of(int calories, List<UserMealWithExcess> meals) {
        return new Pool(calories, meals);
    }

    public static Pool of(int calories, UserMealWithExcess meal) {

        Pool pool = new Pool(calories, new LinkedList<>());
        pool.meals.add(meal);

        return pool;
    }

    public static Pool of(int calories) {
        return new Pool(calories, new LinkedList<>());
    }

    public int getCalories() {
        return calories;
    }

    public List<UserMealWithExcess> getMeals() {
        return meals;
    }

    public void addCalories(int calories) {
        this.calories = this.calories + calories;
    }

    public void everythingExcessIsTrue() {
        meals.forEach(meal -> meal.setExcess(true));
    }

    @Override
    public String toString() {
        return "[pool]: ("+ calories +") by"+ meals;
    }
}
