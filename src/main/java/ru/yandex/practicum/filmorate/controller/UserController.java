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

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @PostMapping()
    public User addUser(@Valid @RequestBody User newUser) throws ValidationException {
        return userService.addUser(newUser);
    }

    @PutMapping()
    public User updateUser(@RequestBody User newUser) throws ValidationException {
        return userService.updateUser(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable Long id, @PathVariable Long friendId) throws NoDataException {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFromFriends(@PathVariable Long id, @PathVariable Long friendId) throws NoDataException {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> listOfFriend(@PathVariable Long id) throws ValidationException, NoDataException {
        return userService.listOfFriends(id);
    }

    @GetMapping("/{id}")
    public User friendById(@PathVariable Long id) throws NoDataException {
        return userService.getById(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> listOfCommonFriends(@PathVariable Long id, @PathVariable Long otherId) throws NoDataException {
        return userService.listCommonFriends(id, otherId);
    }

    @GetMapping()
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/clear")
    public void clearUsers() {
        userService.clearUsers();
    }


}
