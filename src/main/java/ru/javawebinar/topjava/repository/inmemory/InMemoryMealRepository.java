package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(SecurityUtil.authUserId(), meal));
        save(2, new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                "Завтрак", 500));
        save(2, new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 12, 0),
                "Перекус", 500));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            log.info("meal is new, set id={}, set user_id={}", meal.getId(), meal.getUserId());
            return meal;
        }
        // handle case: update, but not present in storage
        log.debug("trying update meal id={}", meal.getId());
        Meal oldMeal = repository.get(meal.getId());
        if (oldMeal == null) {
            log.debug("meal is not exist id={}", meal.getId());
            return null;
        } else if (!oldMeal.getUserId().equals(userId)) {
            log.debug("not allowed to access old.userId={}, new.userId={}", oldMeal.getUserId(), userId);
            return null;
        }

        meal.setUserId(oldMeal.getUserId());
        repository.put(oldMeal.getId(), meal);
        log.info("meal updated id={}", meal.getId());
        return meal;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return filteredByUserId(new ArrayList<>(repository.values()), userId,
                Comparator.comparing(Meal::getDateTime).reversed());
    }

    @Override
    public Meal get(int userId, int id) {
        Meal meal = repository.get(id);
        return (meal == null || meal.getUserId() == userId) ? meal : null;
    }

    @Override
    public boolean delete(int userId, int id) {
        Meal meal = repository.get(id);
        return meal != null && meal.getUserId() == userId
                && repository.remove(id) != null;
    }

    private static List<Meal> filteredByUserId(List<Meal> mealList, int id, Comparator<Meal> comparator) {
        return mealList.stream()
                .filter(meal -> meal.getUserId().equals(id))
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}

