package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MpaRate;

import java.util.List;

public interface MpaStorage {
    MpaRate getMpaById(Long id);

    List<MpaRate> getAllMpa();
}

