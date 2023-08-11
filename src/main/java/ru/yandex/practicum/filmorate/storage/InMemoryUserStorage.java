package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NoDataException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> userStorage = new HashMap<>();
    private int idCounter = 1;

    @Override
    public User addUser(User newUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }
        newUser.setId(counter());
        userStorage.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User updateUser(User newUser) {
        if (!userStorage.containsKey(newUser.getId())) {
            throw new NoDataException("такого пользователя не существует");
        }
        userStorage.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userStorage.values());
    }

    @Override
    public Optional<User> getById(Long id) {
        if (!userStorage.containsKey(id)) {
            throw new NoDataException("такого пользователя не существует");
        }
        return Optional.of(userStorage.get(id));
    }

    @Override
    public void clearUsers() {
        userStorage.clear();
        idCounter = 1;
    }

    @Override
    public void addUsersToFriend(User user1, User user2) {

    }

    @Override
    public void removeUserFromFriends(User user1, User user2) {

    }

    public int counter() {
        return idCounter++;
    }
}
