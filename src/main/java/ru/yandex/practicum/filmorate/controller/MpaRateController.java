package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRate;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.MpaRateService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
public class MpaRateController {
    private final MpaRateService mpaRateService;

    @GetMapping()
    public Collection<MpaRate> getAllMpa() {
        return mpaRateService.getAllMpa();
    }

    @GetMapping("/{id}")
    public MpaRate getMpaById(@PathVariable Long id) {
        return mpaRateService.getMpaById(id);
    }
}
