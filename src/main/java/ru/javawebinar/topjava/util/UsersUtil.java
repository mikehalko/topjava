package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.UserTo;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
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

    public static User getOneByFilter(List<User> userList, Predicate<User> filter) {
        return userList.stream().filter(filter).findFirst().orElse(null);
    }

    public static List<User> getSorted(List<User> userList, Comparator<User> comparator) {
        return userList.stream().sorted(comparator).collect(Collectors.toList());
    }

    private static UserTo createTo(User user) {
        return new UserTo(user.getId(), user.getRegistered(), user.getName(), user.getEmail(), user.getCaloriesPerDay());
    }
}
