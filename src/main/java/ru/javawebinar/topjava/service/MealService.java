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

    public List<MealTo> getAll(int authUserId, int caloriesPerDay) {
        Collection<Meal> mealsByUser = repository.getAll(authUserId);
        if (mealsByUser == null) throw new NotFoundException("not accessible or not found with user id="+ authUserId);

        return MealsUtil.getTos(new ArrayList<>(mealsByUser), caloriesPerDay);
    }

    public MealTo get(int authUserId, int caloriesPerDay, int id) {
        List<MealTo> mealsByUser = getAll(authUserId, caloriesPerDay);
        MealTo meal = mealsByUser.get(id);
        return mealsByUser.get(meal.getId());
    }

    public MealTo create(int authUserId, int caloriesPerDay, Meal meal) {
        log.debug("auth={}, cals={}, meal={}", authUserId, caloriesPerDay, meal);
        Meal newMeal = repository.save(authUserId, meal);
        if (newMeal == null) throw new NotFoundException("not accessible or not found with user id="+ authUserId);

        List<MealTo> mealsByUser = getAll(authUserId, caloriesPerDay);
        return mealsByUser.stream().filter(m -> Objects.equals(m.getId(), newMeal.getId())).findAny().orElse(null);
    }

    public void delete(int authUserId, int id) {
        checkNotFoundWithId(repository.delete(authUserId, id), id);
    }

    public void update(int authUserId, Meal meal) {
        checkNotFoundWithId(repository.save(authUserId, meal), meal.getId());
    }
}