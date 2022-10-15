package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealsRepository {
    List<Meal> getAll();

    Meal getOneById(int id);

    void save(Meal meal);

    void remove(int id);
}
