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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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
    public Meal save(int userId, Meal editMeal) {
        if (editMeal.isNew()) {
            editMeal.setId(counter.incrementAndGet());
            editMeal.setUserId(userId);
            log.debug("meal is new, set id={}, set user_id={}", editMeal.getId(), editMeal.getUserId());
            repository.put(editMeal.getId(), editMeal);
            return editMeal;
        }
        // handle case: update, but not present in storage
        log.debug("meal already exist");

        Meal oldMeal = repository.get(editMeal.getId());
        if (!oldMeal.getUserId().equals(userId)) {
            log.debug("not allowed to access old.userId={}, new.userId={}", oldMeal.getUserId(), userId);
            return null;
        }

        editMeal.setUserId(oldMeal.getUserId());
        repository.put(oldMeal.getId(), editMeal);
        return editMeal;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return MealsUtil.getAllById(new ArrayList<>(repository.values()), userId,
                Comparator.comparing(Meal::getDateTime).reversed());
    }

    @Override
    public Meal get( int userId, int id) {
        Meal meal = repository.get(id);
        return meal.getUserId() == userId ? meal : null;
    }

    @Override
    public boolean delete( int userId, int id) {
        Meal meal = repository.get(id);
        return meal.getUserId() == userId && repository.remove(id) != null;
    }
}

