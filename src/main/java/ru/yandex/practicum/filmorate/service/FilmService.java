package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NoDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class FilmService {

    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();

    public void addLikeToFilm(String filmIdString, String userIdString) throws NoDataException {
        if (filmIdString.contains("-") || userIdString.contains("-")) {
            throw new NoDataException("id не может быть отрицательным");
        }
        Long filmId = Long.parseLong(filmIdString);
        Long userId = Long.parseLong(userIdString);
        inMemoryFilmStorage.getFilmById(filmId).getLikedUserId().add(userId);
    }

    public void removeLikeFromFilm(String filmIdString, String userIdString) throws NoDataException {
        if (filmIdString.contains("-") || userIdString.contains("-")) {
            throw new NoDataException("id не может быть отрицательным");
        }
        Long filmId = Long.parseLong(filmIdString);
        Long userId = Long.parseLong(userIdString);
        inMemoryFilmStorage.getFilmById(filmId).getLikedUserId().remove(userId);
    }

    public List<Film> getTopTen(String countString) {
        int count = Integer.parseInt(countString);
        return inMemoryFilmStorage.getAllFilms().stream()
                .sorted(new Comparator<Film>() {
                    @Override
                    public int compare(Film o1, Film o2) {
                        if (o1.getLikedUserId().size() > o2.getLikedUserId().size()) {
                            return -1;
                        } else {
                            return 1;
                        }

                    }
                })
                .limit(count)
                .collect(Collectors.toList());
    }

}
