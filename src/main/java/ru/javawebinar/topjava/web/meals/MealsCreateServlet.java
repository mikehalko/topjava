package ru.javawebinar.topjava.web.meals;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealsService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class MealsCreateServlet extends HttpServlet {
    private static final Logger log = getLogger(MealsCreateServlet.class);
    private final MealsService service = MealsService.singleton();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("GET");

        request.getRequestDispatcher("/meal/new.jsp").forward(request, response);
    }

        @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("POST");
        request.setCharacterEncoding("UTF-8");

        Meal meal = null;
        try {
            meal = MealsUtil.createByRequest(request);
            service.create(meal);

            log.debug("meal=" + meal);
        } catch (Exception e) {
            log.error("EXCEPTION! meal="+ meal);
            log.error("EXCEPTION="+ e.getMessage());
        }


        response.sendRedirect("/topjava/meals");
    }
}