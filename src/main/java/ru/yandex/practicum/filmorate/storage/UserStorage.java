package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {

    User addUser(User newUser) throws ValidationException;

    User updateUser(User newUser) throws ValidationException;

    void clearUsers();
}
