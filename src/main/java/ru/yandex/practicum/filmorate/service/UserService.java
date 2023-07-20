package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NoDataException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final InMemoryUserStorage inMemoryUserStorage;

    public User addUser(User newUser) throws ValidationException {
        return inMemoryUserStorage.addUser(newUser);
    }

    public User updateUser(User newUser) throws ValidationException {
        return inMemoryUserStorage.updateUser(newUser);
    }

    public User getById(Long id) throws NoDataException {
        return inMemoryUserStorage.getById(id);
    }

    public Collection<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    public void clearUsers() {
        inMemoryUserStorage.clearUsers();
    }

    public void addFriend(Long id1, Long id2) throws NoDataException {
        if (id1 < 0 || id2 < 0) {
            throw new NoDataException("id не может быть отрицательным");
        }
        User user1 = inMemoryUserStorage.getById(id1);
        User user2 = inMemoryUserStorage.getById(id2);
        user1.getFriends().add(id2);
        user2.getFriends().add(id1);
    }

    public void removeFriend(Long id1, Long id2) throws NoDataException {
        if (id1 < 0 || id2 < 0) {
            throw new NoDataException("id не может быть отрицательным");
        }
        User user1 = inMemoryUserStorage.getById(id1);
        User user2 = inMemoryUserStorage.getById(id2);
        user1.getFriends().remove(user2.getId());
        user2.getFriends().remove(user1.getId());
    }

    public List<User> listOfFriends(Long userId) throws ValidationException, NoDataException {
        User user = inMemoryUserStorage.getById(userId);
        if (user.getFriends() == null) {
            throw new ValidationException("Список друзей пуст");
        }
        List<User> listOfFriends = new ArrayList<>();
        for (Long friendId : user.getFriends()) {
            listOfFriends.add(inMemoryUserStorage.getById(friendId));
        }
        return listOfFriends;
    }

    public List<User> listCommonFriends(Long id1, Long id2) throws NoDataException {
        if (id1 < 0 || id2 < 0) {
            throw new NoDataException("id не может быть отрицательным");
        }
        User user1 = inMemoryUserStorage.getById(id1);
        User user2 = inMemoryUserStorage.getById(id2);
        return user1.getFriends().stream()
                .filter(id -> user2.getFriends().contains(id))
                .map(id -> {
                    try {
                        return inMemoryUserStorage.getById(id);
                    } catch (NoDataException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
