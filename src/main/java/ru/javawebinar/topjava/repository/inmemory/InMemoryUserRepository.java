package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.UsersUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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
        log.debug("save user={}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            log.debug("user new id={}", user.getId());
            repository.put(user.getId(), user);
            log.debug("put");
            return user;
        }
        log.info("saved user={}", user);

        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get id={}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return UsersUtil.getSorted(new ArrayList<>(repository.values()), (user1, user2) -> {
            if (user1.getName().equals(user2.getName())) {
                return user1.getEmail().compareTo(user2.getEmail());
            }

            return user1.getName().compareTo(user2.getName());
        });
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail email={}", email);

        return UsersUtil.getOneByFilter(new ArrayList<>(repository.values()), user -> user.getEmail().equals(email));
    }
}
