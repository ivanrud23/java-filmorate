package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {

    Film addFilm(Film newFilm) throws ValidationException;

    Film updateFilm(Film newFilm) throws ValidationException;

    void clearFilms();
}
