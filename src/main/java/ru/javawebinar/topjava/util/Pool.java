package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.MealExcess;

import java.util.LinkedList;
import java.util.List;

public class Pool {
    private int calories;
    private final List<MealExcess> meals;

    public Pool(int calories, List<MealExcess> meals) {
        this.calories = calories;
        this.meals = meals;
    }

    public static Pool of(int calories, List<MealExcess> meals) {
        return new Pool(calories, meals);
    }

    public static Pool of(int calories, MealExcess meal) {

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

    public List<MealExcess> getMeals() {
        return meals;
    }

    public void addCalories(int calories) {
        this.calories = this.calories + calories;
    }

    public void allExcessTurnTrue() {
        meals.forEach(meal -> meal.setExcess(true));
    }

    @Override
    public String toString() {
        return "[pool]: ("+ calories +") by"+ meals;
    }
}
