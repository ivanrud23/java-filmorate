package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.NoDataException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User newUser) throws ValidationException;

    User updateUser(User newUser) throws ValidationException;

    List<User> getAllUsers();

    User getById(Long id) throws NoDataException;

    void clearUsers();
}
