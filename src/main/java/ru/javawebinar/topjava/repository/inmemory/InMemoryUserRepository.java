package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.UsersUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Integer, User> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        UsersUtil.users.forEach(this::save);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete id={}", id);
        return repository.remove(id) != null;
    }


    @Override
    public User save(User user) {
        log.debug("save/update user={}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            log.debug("user is new, id={}", user.getId());
            repository.put(user.getId(), user);
            log.info("save new user={}", user);
            return user;
        }

        User updatedUser = repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
        log.info("updated user={}", user);
        return updatedUser;
    }

    @Override
    public User get(int id) {
        log.info("get id={}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return getSorted(new ArrayList<>(repository.values()),
                Comparator.comparing(User::getName).thenComparing(User::getEmail));
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail email={}", email);
        return getOneByFilter(new ArrayList<>(repository.values()),
                user -> user.getEmail().equalsIgnoreCase(email));
    }

    private static User getOneByFilter(List<User> userList, Predicate<User> filter) {
        return userList.stream().filter(filter).findFirst().orElse(null);
    }

    private static List<User> getSorted(List<User> userList, Comparator<User> comparator) {
        return userList.stream().sorted(comparator).collect(Collectors.toList());
    }
}
