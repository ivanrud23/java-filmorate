package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User addUser(User newUser);

    User updateUser(User newUser);

    List<User> getAllUsers();

    User getById(Long id);

    void addUsersToFriend(User user1, User user2);

    void removeUserFromFriends(User user1, User user2);
}
