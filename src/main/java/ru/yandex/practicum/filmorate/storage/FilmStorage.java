package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRate;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film newFilm);

    Film updateFilm(Film newFilm);

    Film getFilmById(Long id);

    MpaRate getMpaById(Long id);

    List<Film> getAllFilms();

    void clearFilms();

    Genre getGenreById(Long id);

    List<MpaRate> getAllMpa();

    List<Genre> getAllGenre();

    void addLikeToFilm(Long filmId, Long userId);

    void removeLikeFromFilm(Long filmId, Long userId);
}
