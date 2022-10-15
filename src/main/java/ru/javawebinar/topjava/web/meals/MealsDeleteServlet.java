package ru.javawebinar.topjava.web.meals;

import org.slf4j.Logger;
import ru.javawebinar.topjava.service.MealsService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class MealsDeleteServlet extends HttpServlet {
    private static final Logger log = getLogger(MealsDeleteServlet.class);
    private final MealsService service = MealsService.singleton();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("GET");

        Integer id = null;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        service.delete(id);
        log.debug("delete id="+ id);

        response.sendRedirect("/topjava/meals");
    }
}
