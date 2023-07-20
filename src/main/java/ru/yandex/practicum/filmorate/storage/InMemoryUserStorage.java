package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NoDataException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> userStorage = new HashMap<>();
    private int idCounter = 1;

    @Override
    public User addUser(User newUser) throws ValidationException {

        if (newUser.getEmail() == null || newUser.getEmail().isBlank() || !newUser.getEmail().contains("@")) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
        if (newUser.getLogin().isBlank() || newUser.getLogin() == null || newUser.getLogin().contains(" ")) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (newUser.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("дата рождения не может быть в будущем");
        }
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }
        newUser.setId(counter());
        userStorage.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User updateUser(User newUser) throws ValidationException {
        if (!userStorage.containsKey(newUser.getId())) {
            throw new ValidationException("такого пользователя не существует");
        }
        userStorage.put(newUser.getId(), newUser);
        return newUser;
    }

    public Collection<User> getAllUsers() {
        return userStorage.values();
    }

    public User getById(Long id) throws NoDataException {
        if (!userStorage.containsKey(id)) {
            throw new NoDataException("такого пользователя не существует");
        }
        return userStorage.get(id);
    }

    @Override
    public void clearUsers() {
        userStorage.clear();
        idCounter = 1;
    }

    public int counter() {
        return idCounter++;
    }
}
