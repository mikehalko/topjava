package ru.javawebinar.topjava.web.meals;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.service.MealsService;
import ru.javawebinar.topjava.util.CaloriesPerDay;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class MealsShowServlet extends HttpServlet {
    private static final Logger log = getLogger(MealsShowServlet.class);
    private final MealsService service = MealsService.singleton();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("GET");
        Integer id = Integer.parseInt(request.getParameter("id"));
        log.debug("id="+ id);

        MealTo meal = service.show(id, CaloriesPerDay.limit());
        log.debug("meal="+ meal);

        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/meal/show.jsp").forward(request, response);
    }
}