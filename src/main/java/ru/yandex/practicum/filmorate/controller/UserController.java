package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public User addUser(@Valid @RequestBody User newUser) {
        return userService.addUser(newUser);
    }

    @PutMapping()
    public User updateUser(@RequestBody User newUser) {
        return userService.updateUser(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFromFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> listOfFriend(@PathVariable Long id) {
        return userService.listOfFriends(id);
    }

    @GetMapping("/{id}")
    public User friendById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> listOfCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.listCommonFriends(id, otherId);
    }

    @GetMapping()
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

}
