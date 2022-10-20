package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeFilter;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {
    private static final Logger log = LoggerFactory.getLogger(MealService.class);
    private final MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public List<MealTo> getAll(int userId, int caloriesPerDay, DateTimeFilter filter) {
        log.debug("userId={}. caloriesPerDay={}, filter={}", userId, caloriesPerDay, filter);
        List<MealTo> result = MealsUtil.getFilteredTos(repository.getAll(userId), caloriesPerDay, filter);
        log.debug("result={}", result);
        return result;
    }

    public Meal get(int userId, int id) {
        Meal meal = repository.get(userId, id);
        return checkNotFoundWithId(meal, id);
    }

    public Meal create(int userId, int caloriesPerDay, Meal meal) {
        log.debug("try to create new meal auth={}, calories={}, meal={}", userId, caloriesPerDay, meal);
        return repository.save(userId, meal);
    }

    public void delete(int userId, int id) {
        checkNotFoundWithId(repository.delete(userId, id), id);
    }

    public void update(int userId, Meal meal) {
        checkNotFoundWithId(repository.save(userId, meal), meal.getId());
    }
}