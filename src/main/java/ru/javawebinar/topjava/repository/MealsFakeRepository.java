package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class MealsFakeRepository implements MealsRepository {
    private final Map<Integer, Meal> mealsMap;
    private int ids = 0;

    private static final Logger log = getLogger(MealsFakeRepository.class);

    {
        ids++;
        mealsMap = new HashMap<>();
        mealsMap.put(ids, new Meal(ids++, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        mealsMap.put(ids, new Meal(ids++, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        mealsMap.put(ids, new Meal(ids++, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        mealsMap.put(ids, new Meal(ids++, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        mealsMap.put(ids, new Meal(ids++, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        mealsMap.put(ids, new Meal(ids++, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        mealsMap.put(ids, new Meal(ids++, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    private MealsFakeRepository() {}

    private static MealsFakeRepository singletonHolder;

    public static MealsFakeRepository singleton() {
        singletonHolder = singletonHolder == null ? new MealsFakeRepository() : singletonHolder;

        return singletonHolder;
    }


    @Override
    public List<Meal> getAll() {
        ArrayList<Meal> meals = new ArrayList<>(mealsMap.values());

        log.debug("meals="+ meals);

        return meals;
    }

    @Override
    public Meal getOneById(int id) {
        Meal meal = mealsMap.get(id);

        log.debug("meal="+ meal);

        return meal;
    }

    @Override
    public void save(Meal saveMeal) {
        if (saveMeal.getId() == 0) {
            log.debug("save new cause id="+ saveMeal.getId());
            saveMeal.setId(ids++);
        }
        mealsMap.put(saveMeal.getId(), saveMeal);
        log.debug("save/edit id="+ saveMeal.getId());
    }

    @Override
    public void remove(int id) {
        Meal removed = mealsMap.remove(id);
        log.debug("removed="+ removed +", id="+ id);
        if (removed == null) {
            log.error("ERROR!");
            throw new RuntimeException("[fake_repository] [remove]: not found id=" + id);
        }
    }
}