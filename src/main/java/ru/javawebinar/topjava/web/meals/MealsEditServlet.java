package ru.javawebinar.topjava.web.meals;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.service.MealsService;
import ru.javawebinar.topjava.util.CaloriesPerDay;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class MealsEditServlet extends HttpServlet {
    private static final Logger log = getLogger(MealsEditServlet.class);
    private final MealsService service = MealsService.singleton();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("GET");
        String idParam = request.getParameter("id");
        log.debug("id="+ idParam);

        try {
            int id = Integer.parseInt(idParam);
            MealTo meal = service.show(id, CaloriesPerDay.limit());
            request.setAttribute("meal", meal);
        } catch (Exception e) {
            log.error("EXCEPTION! id="+ idParam);
            log.error("EXCEPTION! message="+ e.getMessage());
        }



        request.getRequestDispatcher("/meal/edit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("POST");
        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        log.debug("id="+ id);

        Meal meal = null;
        try {
            meal = MealsUtil.createByRequest(request);
            log.debug("edit meal=" + meal);
            service.edit(Integer.parseInt(id), meal);
        } catch (Exception e) {
            log.error("EXCEPTION! meal=" + meal);
            log.error("EXCEPTION=" + e.getMessage());
        }


        response.sendRedirect("/topjava/meals");
    }
}