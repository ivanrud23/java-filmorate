package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film newFilm);

    Film updateFilm(Film newFilm);

    Film getFilmById(Long id);

    List<Film> getAllFilms();

    void addLikeToFilm(Long filmId, Long userId);

    void removeLikeFromFilm(Long filmId, Long userId);
}
