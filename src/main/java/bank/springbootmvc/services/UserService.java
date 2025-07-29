package bank.springbootmvc.services;

import bank.springbootmvc.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    private final Map<Long, User> users;
    private final AtomicLong counter;

    public UserService() {
        this.users = new ConcurrentHashMap<>();
        this.counter = new AtomicLong();
    }

    public User createUser(User user) {
        if (user.id() != null) {
            throw new IllegalArgumentException("ID пользователя должен быть null при создании");
        }

        if (user.pets() != null && !user.pets().isEmpty()) {
            throw new IllegalArgumentException("Список питомцев пользователя должен быть пустым при создании");
        }

        Long id = counter.incrementAndGet();

        User createdUser = new User(
                id,
                user.name(),
                user.email(),
                user.age(),
                new ArrayList<>()
        );

        users.put(id, createdUser);

        return createdUser;
    }

    public User updateUser(User user) {
        if (user.id() == null) {
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }

        if (!users.containsKey(user.id())) {
            throw new NoSuchElementException("Пользователь не найден с id: " + user.id());
        }

        users.put(user.id(), user);

        return user;
    }

    public void deleteUser(Long id) {
        if (!users.containsKey(id)) {
            throw new NoSuchElementException("Пользователь не найден с id: " + id);
        }

        users.remove(id);
    }

    public User getUser(Long id) {
        if (!users.containsKey(id)) {
            throw new NoSuchElementException("Пользователь не найден с id: " + id);
        }

        return users.get(id);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
