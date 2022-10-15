package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealsFakeRepository;
import ru.javawebinar.topjava.service.MealsService;
import ru.javawebinar.topjava.util.CaloriesPerDay;

import java.util.List;
import java.util.Objects;

import static java.lang.System.out;

/**
 * @see <a href="http://topjava.herokuapp.com">Demo application</a>
 * @see <a href="https://github.com/JavaOPs/topjava">Initial project</a>
 */
public class Main {
    public static void main(String[] args) {
        class Request {
            private final MealsService service = MealsService.singleton();
            private Object getAttribute(String attributeName) {
                if (attributeName.equals("list")) {
                    out.println("list");
                    return service.index(CaloriesPerDay.limit());
                }

                return null;
            }
        }

        Request request = new Request();

        List<MealTo> meals = (List<MealTo>) request.getAttribute("list");

        if (meals != null && !meals.isEmpty()) {
            String  trueStr = "!";
            String falseStr = ".";

            for (MealTo meal : meals) {
                String date = meal.getDateTime().toString();
                String desc = meal.getDescription();
                String cal  = String.valueOf(meal.getCalories());
                String excess = meal.isExcess() ? trueStr : falseStr;

                String line = "<tr>" +
                        "<td>"+ date +"</td>" +
                        "<td>"+ desc +"</td>" +
                        "<td>"+ cal +"</td>" +
                        "<td>"+ excess +"</td>" +
                        "</tr>\n";

                out.print(line);
            }
        } else {
            out.print("Список пуст!");
        }
    }
}
