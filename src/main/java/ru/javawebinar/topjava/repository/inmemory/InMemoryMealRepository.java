package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, SecurityUtil.authUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage

        Meal result = repository.get(meal.getId());

        return result.getUserId().equals(userId) ? repository.put(result.getId(), meal) : null;
    }

    @Override
    public Collection<Meal> getAllByUserId(int userId) {
        Collection<Meal> result =MealsUtil.getAllById(new ArrayList<>(repository.values()), userId,
                Comparator.comparing(Meal::getDateTime).reversed());

        return result.isEmpty() ? null : result;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);

        return meal.getUserId() == userId ? meal : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal meal = repository.get(id);

        return meal.getUserId() == userId && repository.remove(id) != null;
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.values();
    }
}

