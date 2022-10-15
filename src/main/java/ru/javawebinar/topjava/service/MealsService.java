package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealsFakeRepository;
import ru.javawebinar.topjava.repository.MealsRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;


public class MealsService {
    private static final Logger log = getLogger(MealsService.class);
    private static final MealsRepository repository = MealsFakeRepository.singleton();

    private MealsService() {}
    private static MealsService singletonHolder;
    public static MealsService singleton() {
        singletonHolder = singletonHolder == null ? new MealsService() : singletonHolder;

        return singletonHolder;
    }


    public List<MealTo> index(Integer caloriesPerDay) {
        log.debug("index");
        List<Meal> meals = repository.getAll();
        log.debug("meals<Meal>="+ meals);
        List<MealTo> result = MealsUtil.filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, caloriesPerDay);

        log.debug("result<MealTo>="+ meals);

        return result;
    }

    public MealTo show(Integer id, int caloriesPerDay) {
        List<Meal> meals = repository.getAll();
        return MealsUtil.filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, caloriesPerDay)
                .stream()
                .filter(meal -> Objects.equals(meal.getId(), id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("[MealsService] [show] not found id="+ id));
    }

    public void create(Meal meal) {
        repository.save(meal);
    }

    public void delete(Integer id) {
        repository.remove(id);
    }

    public void edit(Integer id, Meal editedMeal) {
        Meal meal = repository.getOneById(id);
        update(meal, editedMeal);

        repository.save(editedMeal);
    }

    private void update(Meal meal, Meal editedMeal) {
        meal.setCalories(editedMeal.getCalories());
        meal.setDateTime(editedMeal.getDateTime());
        meal.setDescription(editedMeal.getDescription());
    }
}
