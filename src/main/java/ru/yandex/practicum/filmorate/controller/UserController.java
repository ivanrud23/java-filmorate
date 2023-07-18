package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.NoDataException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    UserService userService = new UserService();

    @PostMapping()
    public User addUser(@Valid @RequestBody User newUser) throws ValidationException {
        return userService.getInMemoryUserStorage().addUser(newUser);
    }

    @PutMapping()
    public User updateUser(@RequestBody User newUser) throws ValidationException {
        return userService.getInMemoryUserStorage().updateUser(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable String id, @PathVariable String friendId) throws ValidationException, NoDataException {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFromFriends(@PathVariable String id, @PathVariable String friendId) throws ValidationException, NoDataException {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> listOfFriend(@PathVariable String id) throws ValidationException, NoDataException {
        return userService.listOfFriends(id);
    }

    @GetMapping("/{id}")
    public User friendById(@PathVariable String id) throws ValidationException, NoDataException {
        return userService.getInMemoryUserStorage().getById(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> listOfCommonFriends(@PathVariable String id, @PathVariable String otherId) throws ValidationException, NoDataException {
        return userService.listCommonFriends(id, otherId);
    }

    @GetMapping()
    public Collection<User> getAllUsers() {
        return userService.getInMemoryUserStorage().getAllUsers();
    }

    @GetMapping("/clear")
    public void clearUsers() {
        userService.getInMemoryUserStorage().clearUsers();
    }


}
