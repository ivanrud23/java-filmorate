package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

    public Genre getGenreById(Long id) {
        return filmStorage.getGenreById(id);
    }

    public List<Genre> getAllGenre() {
        return filmStorage.getAllGenre();
    }
}
