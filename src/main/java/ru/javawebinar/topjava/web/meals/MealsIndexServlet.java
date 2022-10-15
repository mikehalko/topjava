package ru.javawebinar.topjava.web.meals;

import org.slf4j.Logger;
import ru.javawebinar.topjava.service.MealsService;
import ru.javawebinar.topjava.util.CaloriesPerDay;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class MealsIndexServlet extends HttpServlet {
    private static final Logger log = getLogger(MealsIndexServlet.class);
    private final MealsService service = MealsService.singleton();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("GET");

        request.setAttribute("list", service.index(CaloriesPerDay.limit()));
        request.getRequestDispatcher("/meal/index.jsp").forward(request, response);
    }
}
