package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.NoDataException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film newFilm) throws ValidationException;

    Film updateFilm(Film newFilm) throws ValidationException;

    Film getFilmById(Long id) throws NoDataException;

    List<Film> getAllFilms();

    void clearFilms();
}
