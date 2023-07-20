package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NoDataException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;

    public Film addFilm(Film newFilm) throws ValidationException {
        return inMemoryFilmStorage.addFilm(newFilm);
    }

    public Film updateFilm(Film newFilm) throws ValidationException {
        return inMemoryFilmStorage.updateFilm(newFilm);
    }

    public Collection<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    public Film getFilmById(Long id) throws NoDataException {
        return inMemoryFilmStorage.getFilmById(id);
    }

    public void clearFilms() {
        inMemoryFilmStorage.clearFilms();
    }

    public void addLikeToFilm(Long filmId, Long userId) throws NoDataException {
        if (filmId < 0 || userId < 0) {
            throw new NoDataException("id не может быть отрицательным");
        }
        inMemoryFilmStorage.getFilmById(filmId).getLikedUserId().add(userId);
    }

    public void removeLikeFromFilm(Long filmId, Long userId) throws NoDataException {
        if (filmId < 0 || userId < 0) {
            throw new NoDataException("id не может быть отрицательным");
        }
        inMemoryFilmStorage.getFilmById(filmId).getLikedUserId().remove(userId);
    }

    public List<Film> getTopFilm(Integer count) {
        return inMemoryFilmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikedUserId().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

}
