package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExcess> resultList = new ArrayList<>();
        Map<LocalDate, Pool> poolByDateMap = new HashMap<>();

        // O(N)
        meals.forEach(userMeal -> { // O(N)

            LocalDate date = userMeal.getDateTime().toLocalDate();
            LocalTime time = userMeal.getDateTime().toLocalTime();
            int   calories = userMeal.getCalories();

            Pool pool = poolByDateMap.get(date); // O(1)
            if (pool == null) {
                Pool newPool = Pool.of(0);
                poolByDateMap.put(date, newPool);
                pool = newPool;
            }

            pool.addCalories(calories);

            boolean timeFilter =  time.isAfter(startTime) && time.isBefore(endTime);
            if (timeFilter) {
                addMealWithExcess(userMeal, pool, resultList);
            }
        });


        // O(N)
        poolByDateMap.forEach((date, pool) -> { // O(N) - если N записей и каждая на разный день
            boolean excess = pool.getCalories() > caloriesPerDay;
            if (excess) {
                pool.everythingExcessIsTrue(); // O(N) - если в одном дне будет N записей, тогда и внешний цикл O(1)
            }
        });

        return resultList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        return null;
    }

    public static void addMealWithExcess(UserMeal userMeal, Pool pool, List<UserMealWithExcess> list) {
        UserMealWithExcess userMealWithExcess = new UserMealWithExcess(
                userMeal.getDateTime(),
                userMeal.getDescription(),
                userMeal.getCalories(),
                false
        );

        pool.getMeals().add(userMealWithExcess);
        list.add(userMealWithExcess);
    }
}
