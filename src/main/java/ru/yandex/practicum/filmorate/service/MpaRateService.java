package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRate;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MpaRateService {

    private final MpaStorage mpaStorage;

    public List<MpaRate> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

    public MpaRate getMpaById(Long id) {
        return mpaStorage.getMpaById(id);
    }
}