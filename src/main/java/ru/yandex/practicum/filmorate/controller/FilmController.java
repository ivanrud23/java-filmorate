package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> filmStorage = new HashMap<>();
    private static int idCounter = 1;


    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film newFilm, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            if (newFilm.getName().isBlank() || newFilm.getName() == null) {
                log.info("Название не может быть пустым");
                throw new ValidationException("Название не может быть пустым");
            }
            if (newFilm.getDescription().length() > 200) {
                log.info("Превышена максимальная длина описания — 200 символов");
                throw new ValidationException("Превышена максимальная длина описания — 200 символов");
            }
            if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                log.info("дата релиза не может быть раньше 28 декабря 1895 года");
                throw new ValidationException("дата релиза не может быть раньше 28 декабря 1895 года");
            }
            if (newFilm.getDuration() < 1) {
                log.info("продолжительность фильма должна быть положительной");
                throw new ValidationException("продолжительность фильма должна быть положительной");
            }
        }
        newFilm.setId(counter());
        filmStorage.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film newFilm) throws ValidationException {
        if (!filmStorage.containsKey(newFilm.getId())) {
            throw new ValidationException("такого фильма не существует");
        }
        filmStorage.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @GetMapping()
    public Collection<Film> getAllFilms() {
        return filmStorage.values();
    }

    @GetMapping("/clear")
    public void clearUsers() {
        filmStorage.clear();
        idCounter = 1;
    }

    public static int counter() {
        return idCounter++;
    }
}
