package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NoDataException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public User addUser(User newUser) {
        return userStorage.addUser(newUser);
    }

    public User updateUser(User newUser) {
        return userStorage.updateUser(newUser);
    }

    public User getById(Long id) {
        return userStorage.getById(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(Long id1, Long id2) {
        if (id1 < 0 || id2 < 0) {
            throw new NoDataException("id не может быть отрицательным");
        }
        User user1 = userStorage.getById(id1);
        User user2 = userStorage.getById(id2);
        user1.getFriends().add(id2);
        user2.getFriends().add(id1);
        userStorage.addUsersToFriend(user1, user2);
    }

    public void removeFriend(Long id1, Long id2) {
        if (id1 < 0 || id2 < 0) {
            throw new NoDataException("id не может быть отрицательным");
        }
        User user1 = userStorage.getById(id1);
        User user2 = userStorage.getById(id2);
        user1.getFriends().remove(user2.getId());
        user2.getFriends().remove(user1.getId());
        userStorage.removeUserFromFriends(user1, user2);
    }

    public List<User> listOfFriends(Long userId) {
        User user = userStorage.getById(userId);
        if (user.getFriends() == null) {
            throw new ValidationException("Список друзей пуст");
        }
        return user.getFriends().stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    public List<User> listCommonFriends(Long id1, Long id2) {
        if (id1 < 0 || id2 < 0) {
            throw new NoDataException("id не может быть отрицательным");
        }
        User user1 = userStorage.getById(id1);
        User user2 = userStorage.getById(id2);
        return user1.getFriends().stream()
                .filter(id -> user2.getFriends().contains(id))
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }


}
