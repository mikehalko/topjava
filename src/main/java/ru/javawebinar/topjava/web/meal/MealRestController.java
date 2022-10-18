package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }


    public List<MealTo> getAll() {
        log.info("getAll");

        return service.getAll(authUserId(), authUserCaloriesPerDay());
    }


    public MealTo get(int id) {
        log.info("get id={}", id);

        return service.get(authUserId(), authUserCaloriesPerDay(), id);
    }


    public MealTo create(MealTo mealTo) {
        log.info("create meal={}", mealTo);
        if (mealTo == null) throw new RuntimeException("meal=null");

        Meal meal = MealsUtil.convertDTO(mealTo);
        checkNew(meal);
        return service.create(authUserId(), authUserCaloriesPerDay(), meal);
    }

    public void delete(int id) {
        log.info("delete id={}", id);
        service.delete(authUserId(), authUserCaloriesPerDay(), id);
    }

    public void update(MealTo mealTo, int id) {
        log.info("update meal={} with id={}", mealTo, id);
        if (mealTo == null) throw new RuntimeException("meal=null");

        Meal meal = MealsUtil.convertDTO(mealTo);
        assureIdConsistent(meal, id);
        service.update(authUserId(), meal);
    }
}