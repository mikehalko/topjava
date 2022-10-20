package ru.javawebinar.topjava;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.DateTimeFilter;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDateTime;
import java.util.Arrays;

public class SpringMain {
    public static final SpringMain Holder = new SpringMain("spring/spring-app.xml");
    public final ApplicationContext applicationContext;
    public SpringMain(String appCtxPath) {
        applicationContext = new ClassPathXmlApplicationContext(appCtxPath);
    }
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            System.out.println("[TEST SPRING MAIN]: meal rest controller test:");
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            DateTimeFilter filter = new DateTimeFilter();
            System.out.println("[TEST SPRING MAIN]: getAll ="+ mealRestController.getAll(filter));
            System.out.println("[TEST SPRING MAIN]: get 1 ="+ mealRestController.get(1));

            LocalDateTime dateTime =  LocalDateTime.of(2000, 1, 1, 1, 1, 1);
            Meal meal = new Meal(null, dateTime, "test", 2000);
            System.out.println("[TEST SPRING MAIN]: create ="+ mealRestController.create(meal));
            System.out.println("update id=8");
            mealRestController.update(meal, 8);
            System.out.println("[TEST SPRING MAIN]: getAll ="+ mealRestController.getAll(filter));

            System.out.println("delete id=8");
            mealRestController.delete(8);

            System.out.println("[TEST SPRING MAIN]: getAll ="+ mealRestController.getAll(filter));
        }
    }
}
