package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MealsUtil {
    public static void main(String[] args) {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<MealExcess> mealsCyclesOption =
                filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(13, 1), 2000);
        System.out.println("[cycles-option]:");
        mealsCyclesOption.forEach(meal -> System.out.println("\t"+ meal));

        System.out.println("-------------");

        List<MealExcess> mealsStreamOption =
                filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(13, 1), 2000);
        System.out.println("[stream-option]:");
        mealsStreamOption.forEach(meal -> System.out.println("\t"+ meal));


        System.out.println("-------------");
        List<MealExcess> mealsStreamAggregate =
                filteredByStreamsAggregate(meals, LocalTime.of(7, 0), LocalTime.of(13, 1), 2000);
        System.out.println("[stream-aggregate]:");
        mealsStreamAggregate.forEach(meal -> System.out.println("\t"+ meal));


        System.out.println("-------------");
        List<MealExcess> mealsStreamInOneReturn =
                filteredByStreamsInOneReturn(meals, LocalTime.of(7, 0), LocalTime.of(13, 1), 2000);
        System.out.println("[stream-in_one_return]:");
        mealsStreamInOneReturn.forEach(meal -> System.out.println("\t"+ meal));
    }

    public static List<MealExcess> filteredByCyclesMy(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // 0.a Результирующий список
        // 0.b карта pool (содержит сгруппированные meals по дате), чтобы менять excess всех, кто в одном pool
        List<MealExcess> resultList = new ArrayList<>();
        Map<LocalDate, Pool> poolMap = new HashMap<>();

        // 1. Проход по списку, 2. Суммирование каллорий в pool, 3. Фильтрация, 4. add в результирующий список
        // 1.
        meals.forEach(meal -> { // O(N)

            LocalDate date = meal.getDateTime().toLocalDate();
            int   calories = meal.getCalories();

            Pool pool = safeGetPoolByDate(poolMap, date);
            pool.addCalories(calories); // 2.

            Filter filter = Filter.of(startTime, endTime);
            if (filter.between(meal)) { // 3.
                MealExcess mealExcess = MealExcess.of(meal, false);
                pool.getMeals().add(mealExcess); // в pool, чтобы в последствии менять поле excess
                resultList.add(mealExcess); // 4.
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

    public static List<MealExcess> filteredByCycles(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Filter filter = Filter.of(startTime, endTime);

        Map<LocalDate, Integer> sumByDay = new HashMap<>();
        for (Meal meal : meals) {
            sumByDay.put(meal.getDate(), sumByDay.getOrDefault(meal.getDate(), 0) + meal.getCalories());
        }

        List<MealExcess> mealsExcess = new ArrayList<>();
        for (Meal meal : meals) {
            if (filter.between(meal)) {
                mealsExcess.add(MealExcess.of(meal, sumByDay.get(meal.getDate()) > caloriesPerDay));
            }
        }

        return mealsExcess;
    }

    public static List<MealExcess> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Filter filter = Filter.of(startTime, endTime);

        // 1 Разибение по группам (дни)
        Map <LocalDate, List<Meal>> mealsGroups = meals.stream()
                        .sorted(Comparator.comparing(Meal::getDateTime))
                        .collect(Collectors.groupingBy(Meal::getDate));

        // 2. Выявление условия (calories > caloriesPerDay) и конвертация в UserMealWithExcess
        List<MealExcess> mealExcList = new ArrayList<>();
        mealsGroups.forEach( (date, mealList) -> {
            boolean mark = mealsCaloriesGreaterThanN(mealList, caloriesPerDay);
            mealList.forEach( meal -> mealExcList.add(MealExcess.of(meal, mark)) );
        });

        // 3. Фильтрация по заданному фильтру
        List<MealExcess> filteredMeals = mealExcList.stream()
                .filter(filter::between)
                .sorted(Comparator.comparing(MealExcess::getDateTime))
                .collect(Collectors.toList());

        return filteredMeals;
    }

    public static List<MealExcess> filteredByStreamsAggregate(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Filter filter = Filter.of(startTime, endTime);

        final class Aggregate {
            private final List<Meal> dailyMeals = new ArrayList<>();
            private int dailySumOfCalories;

            private void accumulate(Meal meal) {
                dailySumOfCalories += meal.getCalories();
                if (filter.between(meal)) {
                    dailyMeals.add(meal);
                }
            }

            private Aggregate combine(Aggregate that) {
                this.dailySumOfCalories += that.dailySumOfCalories;
                this.dailyMeals.addAll(that.dailyMeals);

                return this;
            }

            private Stream<MealExcess> finisher() {
                final boolean exceed = dailySumOfCalories > caloriesPerDay;
                return dailyMeals.stream().map(meal -> MealExcess.of(meal, exceed));
            }
        }

        Collection<Stream<MealExcess>> values = meals.stream()
                .parallel()
                .collect(Collectors.groupingBy(Meal::getDate,
                         Collector.of(Aggregate::new, Aggregate::accumulate, Aggregate::combine, Aggregate::finisher)))
                .values();


        return values.stream().flatMap(y -> y).collect(Collectors.toList());
    }

    public static List<MealExcess> filteredByStreamsInOneReturn(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate))
                .values()
                .stream()
                .flatMap(dayMeals -> {
                    boolean exceed = dayMeals.stream()
                            .mapToInt(Meal::getCalories)
                            .sum() > caloriesPerDay;

                    return dayMeals.stream()
                            .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                            .map(meal -> MealExcess.of(meal, exceed));
                })
                .collect(Collectors.toList());
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
    private static boolean mealsCaloriesGreaterThanN(List<Meal> meals, final Integer N) {
        int sum = meals.stream().mapToInt(Meal::getCalories).sum();
        return sum > N;
    }
}
