package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashMap<Integer, User> userStorage = new HashMap<>();
    private static int idCounter = 1;


    @PostMapping()
    public User addUser(@Valid @RequestBody User newUser, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            if (newUser.getEmail() == null || newUser.getEmail().isBlank() || !newUser.getEmail().contains("@")) {
                log.info("электронная почта не может быть пустой и должна содержать символ @");
                throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
            }
            if (newUser.getLogin().isBlank() || newUser.getLogin() == null || newUser.getLogin().contains(" ")) {
                log.info("логин не может быть пустым или содержать пробелы");
                throw new ValidationException("логин не может быть пустым и содержать пробелы");
            }
            if (newUser.getBirthday().isAfter(LocalDate.now())) {
                log.info("дата рождения не может быть в будущем");
                throw new ValidationException("дата рождения не может быть в будущем");
            }
            if (newUser.getName() == null || newUser.getName().isBlank()) {
                newUser.setName(newUser.getLogin());
            }
        }
        newUser.setId(counter());
        userStorage.put(newUser.getId(), newUser);
        return newUser;
    }

    @PutMapping()
    public User updateUser(@RequestBody User newUser) throws ValidationException {
        if (!userStorage.containsKey(newUser.getId())) {
            throw new ValidationException("такого пользователя не существует");
        }
        userStorage.put(newUser.getId(), newUser);
        return newUser;
    }

    @GetMapping()
    public Collection<User> getAllUsers() {
        return userStorage.values();
    }

    @GetMapping("/clear")
    public void clearUsers() {
        userStorage.clear();
        idCounter = 1;
    }

    public static int counter() {
        return idCounter++;
    }
}
