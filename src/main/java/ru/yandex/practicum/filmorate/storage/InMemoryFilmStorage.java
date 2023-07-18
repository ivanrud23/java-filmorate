package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NoDataException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Map<Long, Film> filmStorage = new HashMap<>();
    private static int idCounter = 1;


    public Film addFilm(Film newFilm) throws ValidationException {
        if (newFilm.getName().isBlank() || newFilm.getName() == null) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (newFilm.getDescription().length() > 200) {
            throw new ValidationException("Превышена максимальная длина описания — 200 символов");
        }
        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (newFilm.getDuration() < 1) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
        newFilm.setId(counter());
        filmStorage.put(newFilm.getId(), newFilm);
        return newFilm;
    }


    public Film updateFilm(Film newFilm) throws ValidationException {
        if (!filmStorage.containsKey(newFilm.getId())) {
            throw new ValidationException("такого фильма не существует");
        }
        filmStorage.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    public Film getFilmById(Long id) throws NoDataException {
        if (!filmStorage.containsKey(id)) {
            throw new NoDataException("Film with id = " + id + "not exist");
        }
        return filmStorage.get(id);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.values();
    }

    public int counter() {
        return idCounter++;
    }

    public void clearFilms() {
        filmStorage.clear();
        idCounter = 1;
    }
}
