package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {
    private static final Logger log = LoggerFactory.getLogger(MealService.class);
    private final MealRepository repository;
    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public List<MealTo> getAll(int userId, int caloriesPerDay) {
        Collection<Meal> mealsByUser = repository.getAll(userId);
        if (mealsByUser == null) throw new NotFoundException("not accessible or not found with user id="+ userId);

        return MealsUtil.getTos(new ArrayList<>(mealsByUser), caloriesPerDay);
    }

    public MealTo get(int userId, int caloriesPerDay, int id) {
        List<MealTo> mealsByUser = getAll(userId, caloriesPerDay);
        MealTo meal = mealsByUser.get(id);
        return mealsByUser.get(meal.getId());
    }

    public MealTo create(int userId, int caloriesPerDay, Meal meal) {
        log.debug("auth={}, cals={}, meal={}", userId, caloriesPerDay, meal);
        Meal newMeal = repository.save(userId, meal);
        if (newMeal == null) throw new NotFoundException("not accessible or not found with user id="+ userId);

        List<MealTo> mealsByUser = getAll(userId, caloriesPerDay);
        return mealsByUser.stream().filter(m -> Objects.equals(m.getId(), newMeal.getId())).findAny().orElse(null);
    }

    public void delete(int userId, int id) {
        checkNotFoundWithId(repository.delete(userId, id), id);
    }

    public void update(int userId, Meal meal) {
        checkNotFoundWithId(repository.save(userId, meal), meal.getId());
    }
}