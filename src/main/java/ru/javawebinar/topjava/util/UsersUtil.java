package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UsersUtil {
    public static final List<User> users = Arrays.asList(
            new User(null, "Олег", "oleg@email.com", "oleg", Role.USER),
            new User(null, "Иван", "ivan@email.com", "ivan", Role.USER),
            new User(null, "Анна", "anna@email.com", "anna", Role.USER)
    );
}
