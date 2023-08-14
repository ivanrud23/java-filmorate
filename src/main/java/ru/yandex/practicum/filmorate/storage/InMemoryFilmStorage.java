package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NoDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Map<Long, Film> filmStorage = new HashMap<>();
    private static int idCounter = 1;

    @Override
    public Film addFilm(Film newFilm) {
        newFilm.setId(counter());
        filmStorage.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (!filmStorage.containsKey(newFilm.getId())) {
            throw new NoDataException("такого фильма не существует");
        }
        filmStorage.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film getFilmById(Long id) {
        if (!filmStorage.containsKey(id)) {
            throw new NoDataException("Film with id = " + id + "not exist");
        }
        return filmStorage.get(id);
    }

    @Override
    public MpaRate getMpaById(Long id) {
        return null;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmStorage.values());
    }

    public int counter() {
        return idCounter++;
    }

    @Override
    public Genre getGenreById(Long id) {
        return null;
    }

    @Override
    public List<MpaRate> getAllMpa() {
        return null;
    }

    @Override
    public List<Genre> getAllGenre() {
        return null;
    }

    @Override
    public void addLikeToFilm(Long filmId, Long userId) {

    }

    @Override
    public void removeLikeFromFilm(Long filmId, Long userId) {

    }
}
