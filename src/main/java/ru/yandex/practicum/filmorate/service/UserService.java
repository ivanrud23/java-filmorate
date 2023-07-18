package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
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
@Data
@RequiredArgsConstructor
public class UserService {

    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    public void addFriend(String id1, String id2) throws ValidationException, NoDataException {
        if (id1.contains("-") || id2.contains("-")) {
            throw new NoDataException("id не может быть отрицательным");
        }
        User user1 = inMemoryUserStorage.getById(id1);
        User user2 = inMemoryUserStorage.getById(id2);
        user1.getFriends().add(Long.parseLong(id2));
        user2.getFriends().add(Long.parseLong(id1));
    }

    public void removeFriend(String id1, String id2) throws ValidationException, NoDataException {
        if (id1.contains("-") || id2.contains("-")) {
            throw new ValidationException("id не может быть отрицательным");
        }
        User user1 = inMemoryUserStorage.getById(id1);
        User user2 = inMemoryUserStorage.getById(id2);
        user1.getFriends().remove(user2.getId());
        user2.getFriends().remove(user1.getId());
    }

    public Collection<User> listOfFriends(String userString) throws ValidationException, NoDataException {
        User user = inMemoryUserStorage.getById(userString);
        if (user.getFriends()==null) {
            throw new ValidationException("Список друзей пуст");
        }
        List<User> listOfFriends = new ArrayList<>();
        for (Long friendId : user.getFriends()) {
            listOfFriends.add(inMemoryUserStorage.getById(friendId.toString()));
        }
        return listOfFriends;
    }

    public Collection<User> listCommonFriends(String id1, String id2) throws ValidationException, NoDataException {
        if (id1.contains("-") || id2.contains("-")) {
            throw new ValidationException("id не может быть отрицательным");
        }
        User user1 = inMemoryUserStorage.getById(id1);
        User user2 = inMemoryUserStorage.getById(id2);
        List<User> listOfFriends = new ArrayList<>();
        List<Long> listOfId = user1.getFriends().stream()
                .filter(id -> user2.getFriends().contains(id))
                .collect(Collectors.toList());
        for (Long id : listOfId) {
            listOfFriends.add(inMemoryUserStorage.getById(id.toString()));
        }
        return listOfFriends;
    }
}
