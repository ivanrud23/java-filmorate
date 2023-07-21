package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NoDataException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Map<Long, Film> filmStorage = new HashMap<>();
    private static int idCounter = 1;

    @Override
    public Film addFilm(Film newFilm) throws ValidationException {
        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("дата релиза не может быть раньше 28 декабря 1895 года");
        }
        newFilm.setId(counter());
        filmStorage.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film updateFilm(Film newFilm) throws ValidationException {
        if (!filmStorage.containsKey(newFilm.getId())) {
            throw new NoDataException("такого фильма не существует");
        }
        filmStorage.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film getFilmById(Long id) throws NoDataException {
        if (!filmStorage.containsKey(id)) {
            throw new NoDataException("Film with id = " + id + "not exist");
        }
        return filmStorage.get(id);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmStorage.values());
    }

    public int counter() {
        return idCounter++;
    }

    @Override
    public void clearFilms() {
        filmStorage.clear();
        idCounter = 1;
    }
}
