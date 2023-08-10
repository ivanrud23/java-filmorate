package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRate;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaRateService {

    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

    public List<MpaRate> getAllMpa() {
        return filmStorage.getAllMpa();
    }
    public MpaRate getMpaById(Long id) {
        return filmStorage.getMpaById(id);
    }
}