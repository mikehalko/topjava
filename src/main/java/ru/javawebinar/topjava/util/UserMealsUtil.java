package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

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

        List<UserMealWithExcess> mealsCyclesOption =
                filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(13, 1), 2000);
        System.out.println("[cycles-option]:");
        mealsCyclesOption.forEach(meal -> System.out.println("\t"+ meal));

        System.out.println("-------------");

        List<UserMealWithExcess> mealsStreamOption =
                filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(13, 1), 2000);
        System.out.println("[stream-option]:");
        mealsStreamOption.forEach(meal -> System.out.println("\t"+ meal));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // 0.a Результирующий список
        // 0.b карта pool (содержит сгруппированные meals по дате), чтобы менять excess всех, кто в одном pool
        List<UserMealWithExcess> resultList = new ArrayList<>();
        Map<LocalDate, Pool> poolMap = new HashMap<>();

        // 1. Проход по списку, 2. Суммирование каллорий в pool, 3. Фильтрация, 4. add в результирующий список
        // 1.
        meals.forEach(userMeal -> { // O(N)

            LocalDate date = userMeal.getDateTime().toLocalDate();
            int   calories = userMeal.getCalories();

            Pool pool = safeGetPoolByDate(poolMap, date);
            pool.addCalories(calories); // 2.

            Filter filter = Filter.of(startTime, endTime);
            if (filter.between(userMeal)) { // 3.
                UserMealWithExcess userMealWithExcess = UserMealWithExcess.of(userMeal, false);
                pool.getMeals().add(userMealWithExcess); // в pool, чтобы в последствии менять поле excess
                resultList.add(userMealWithExcess); // 4.
            }
        });


        // Проход, свитч поля excess по условию у всех, кто в одном pool (по дню)
        poolMap.forEach((date, pool) -> { // O(N) - если N записей и каждая на разный день
            boolean excess = pool.getCalories() > caloriesPerDay;
            if (excess) {
                pool.allExcessTurnTrue(); // O(N) - если в одном дне будет N записей, тогда и внешний цикл O(1)
            }
        });

        return resultList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Filter filter = Filter.of(startTime, endTime);

        // 1 Разибение по группам (дни)
        Map <LocalDate, List<UserMeal>> mealsGroups = meals.stream()
                        .sorted(Comparator.comparing(UserMeal::getDateTime))
                        .collect(Collectors.groupingBy(UserMeal::getDate));

        // 2. Выявление условия (calories > caloriesPerDay) и конвертация в UserMealWithExcess
        List<UserMealWithExcess> mealExcList = new ArrayList<>();
        mealsGroups.forEach( (date, mealList) -> {
            boolean mark = mealsCaloriesGreaterThanN(mealList, caloriesPerDay);
            mealList.forEach( meal -> mealExcList.add(UserMealWithExcess.of(meal, mark)) );
        });

        // 3. Фильтрация по заданному фильтру
        List<UserMealWithExcess> filteredMeals = mealExcList.stream()
                .filter(filter::between)
                .sorted(Comparator.comparing(UserMealWithExcess::getDateTime))
                .collect(Collectors.toList());

        return filteredMeals;
    }


    // получить pool по дате из карты, если null, вернуть новый, добавив в карту
    private static Pool safeGetPoolByDate(Map<LocalDate, Pool> poolMap, LocalDate date) {
        Pool pool = poolMap.get(date); // O(1)
        if (pool == null) {
            Pool newPool = Pool.of(0);
            poolMap.put(date, newPool);
            pool = newPool;
        }

        return pool;
    }


    // проверка условия, если сумма всех полей calories из списка meals > N, вернет true
    private static boolean mealsCaloriesGreaterThanN(List<UserMeal> meals, final Integer N) {
        int sum = meals.stream().mapToInt(UserMeal::getCalories).sum();
        return sum > N;
    }
}
