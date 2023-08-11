package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NoDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FilmService {

    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

    public Film addFilm(Film newFilm) {
        return filmStorage.addFilm(newFilm);
    }

    public Film updateFilm(Film newFilm) {
        return filmStorage.updateFilm(newFilm);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }


    public void addLikeToFilm(Long filmId, Long userId) {
        if (filmId < 0 || userId < 0) {
            throw new NoDataException("id не может быть отрицательным");
        }
        filmStorage.getFilmById(filmId).getLikedUserId().add(userId);
        filmStorage.addLikeToFilm(filmId, userId);
    }

    public void removeLikeFromFilm(Long filmId, Long userId) {
        if (filmId < 0 || userId < 0) {
            throw new NoDataException("id не может быть отрицательным");
        }
        filmStorage.getFilmById(filmId).getLikedUserId().remove(userId);
        filmStorage.removeLikeFromFilm(filmId, userId);
    }

    public List<Film> getTopFilm(Integer count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikedUserId().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

}
