package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.UserTo;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UsersUtil {
    public static final List<User> users = Arrays.asList(
            new User(null, "Олег", "oleg@email.com", "oleg", Role.USER),
            new User(null, "Иван", "ivan@email.com", "ivan", Role.USER),
            new User(null, "Анна", "anna@email.com", "anna", Role.USER)
    );


    public static List<UserTo> getTos(Collection<User> users) {
        return users.stream()
                .map(UsersUtil::createTo)
                .collect(Collectors.toList());
    }

    private static UserTo createTo(User user) {
        return new UserTo(user.getId(), user.getRegistered(), user.getName(), user.getEmail(), user.getCaloriesPerDay());
    }
}
