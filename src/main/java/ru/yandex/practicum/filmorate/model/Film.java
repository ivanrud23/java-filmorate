package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.FilmRealiseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film {
    private long id;
    @NotBlank(message = "name is mandatory")
    private String name;
    @Size(max = 200)
    private String description;
    @FilmRealiseDate
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private final Set<Long> likedUserId = new HashSet<>();
}
