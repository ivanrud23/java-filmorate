package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {


    private final GenreStorage genreDbStorage;

    public Genre getGenreById(Long id) {
        return genreDbStorage.getGenreById(id);
    }

    public List<Genre> getAllGenre() {
        return genreDbStorage.getAllGenre();
    }
}
