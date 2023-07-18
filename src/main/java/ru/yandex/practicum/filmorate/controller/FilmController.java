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

    FilmService filmService = new FilmService();

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film newFilm) throws ValidationException {
        return filmService.getInMemoryFilmStorage().addFilm(newFilm);
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film newFilm) throws ValidationException {
        return filmService.getInMemoryFilmStorage().updateFilm(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable String id, @PathVariable String userId) throws ValidationException, NoDataException {
        filmService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable String id, @PathVariable String userId) throws ValidationException, NoDataException {
        filmService.removeLikeFromFilm(id, userId);
    }

    @GetMapping()
    public Collection<Film> getAllFilms() {
        return filmService.getInMemoryFilmStorage().getAllFilms();
    }

    @GetMapping("/popular")
    public Collection<Film> rateFilms(@RequestParam(defaultValue = "10") String count) throws ValidationException {
        return filmService.getTopTen(count);
    }


    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable(name = "id", required = false) String idString) throws ValidationException, NoDataException {
        if (idString.isBlank() || idString.isEmpty()) {
            throw new ValidationException("id is empty");
        }
        Long id = Long.parseLong(idString);
        return filmService.getInMemoryFilmStorage().getFilmById(id);

    }

    @GetMapping("/clear")
    public void clearFilms() {
        filmService.getInMemoryFilmStorage().clearFilms();
    }


}
