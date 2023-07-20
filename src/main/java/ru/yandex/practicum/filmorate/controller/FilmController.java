package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.NoDataException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;


@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film newFilm) throws ValidationException {
        return filmService.addFilm(newFilm);
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film newFilm) throws ValidationException {
        return filmService.updateFilm(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable Long id, @PathVariable Long userId) throws NoDataException {
        filmService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable Long id, @PathVariable Long userId) throws NoDataException {
        filmService.removeLikeFromFilm(id, userId);
    }

    @GetMapping()
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/popular")
    public Collection<Film> rateFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getTopFilm(count);
    }


    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable(name = "id", required = false) Long id) throws ValidationException, NoDataException {
        if (id == null) {
            throw new ValidationException("id is empty");
        }
        return filmService.getFilmById(id);

    }

    @GetMapping("/clear")
    public void clearFilms() {
        filmService.clearFilms();
    }


}
