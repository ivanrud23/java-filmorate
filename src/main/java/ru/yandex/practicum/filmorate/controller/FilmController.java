package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film newFilm) {
        return filmService.addFilm(newFilm);
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        return filmService.updateFilm(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLikeFromFilm(id, userId);
    }

    @GetMapping()
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/popular")
    public List<Film> rateFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getTopFilm(count);
    }


    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable(name = "id", required = false) Long id) {
        if (id == null) {
            throw new ValidationException("id is empty");
        }
        return filmService.getFilmById(id);
    }




}
