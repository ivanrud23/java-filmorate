package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NoDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Component
@Data
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;


    @Override
    public Film addFilm(Film newFilm) {
        addFilmToDb(newFilm);
        return newFilm;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (!jdbcTemplate.queryForObject(String.format("SELECT EXISTS (SELECT 1 FROM film WHERE film_id = %d)", newFilm.getId()), Boolean.class)) {
            throw new NoDataException("такого фильма не существует");
        }
        updateFilmToDb(newFilm);
        return newFilm;
    }

    @Override
    public Film getFilmById(Long id) {
        if (!jdbcTemplate.queryForObject(String.format("SELECT EXISTS (SELECT 1 FROM film WHERE film_id = %d)", id), Boolean.class)) {
            throw new NoDataException("такого пользователя не существует");
        }
        return jdbcTemplate.queryForObject("SELECT * FROM film WHERE film_id = ?", (rs, rowNum) -> makeFilm(rs), id);
    }

    @Override
    public List<Film> getAllFilms() {
        return jdbcTemplate.query("SELECT * FROM film", (rs, rowNum) -> makeFilm(rs));
    }


    private void addFilmToDb(Film newFilm) {
        long mpaId = newFilm.getMpa().getId();
        jdbcTemplate.execute(String.format("INSERT INTO film(" +
                        "name, " +
                        "description, " +
                        "release_date, " +
                        "duration, " +
                        "mpa_rate_id) VALUES ('%s', '%s', '%s', %d, %d);",
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                mpaId));
        newFilm.setId(getFilmIdFromFDb());

        if (newFilm.getGenres() != null) {
            jdbcTemplate.execute(String.format("DELETE FROM film_genre WHERE film_id = %d;",
                    newFilm.getId()));
            for (Genre genre : newFilm.getGenres()) {
                jdbcTemplate.execute(String.format("INSERT INTO film_genre VALUES (%d, %d);",
                        newFilm.getId(),
                        genre.getId()));
            }
        }
    }

    private void updateFilmToDb(Film newFilm) {
        long mpaId = newFilm.getMpa().getId();
        jdbcTemplate.execute(String.format("MERGE INTO film(" +
                        "film_id, " +
                        "name, " +
                        "description, " +
                        "release_date, " +
                        "duration, " +
                        "rate, " +
                        "mpa_rate_id) VALUES (%d, '%s', '%s','%s', %d, %d, %d);",
                newFilm.getId(),
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getRate(),
                mpaId));

        if (newFilm.getGenres() != null) {
            jdbcTemplate.execute(String.format("DELETE FROM film_genre WHERE film_id = %d;",
                    newFilm.getId()));
            Set<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
            for (Genre genre : newFilm.getGenres()) {
                genres.add(genreDbStorage.getGenreById(genre.getId().longValue()));
                jdbcTemplate.execute(String.format("INSERT INTO film_genre VALUES (%d, %d);",
                        newFilm.getId(),
                        genre.getId()));
            }
        }
    }


    @Override
    public void addLikeToFilm(Long filmId, Long userId) {
        jdbcTemplate.execute(String.format("INSERT INTO film_likes VALUES (%d, %d)", filmId, userId));
    }

    @Override
    public void removeLikeFromFilm(Long filmId, Long userId) {
        jdbcTemplate.execute(String.format("DELETE FROM film_likes WHERE film_id = %d AND users_id = %d", filmId, userId));
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film newFilm = new Film();
        long id = rs.getLong("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        int rate = rs.getInt("rate");
        MpaRate mpa = jdbcTemplate.queryForObject("SELECT * FROM mpa_rate WHERE mpa_rate_id = ?",
                (rs1, rowNum) -> mpaDbStorage.makeMpaFromDb(rs1), rs.getInt("mpa_rate_id"));
        Set<Genre> genre = new TreeSet<>(Comparator.comparing(Genre::getId));
        genre.addAll(jdbcTemplate.query("SELECT * FROM genre WHERE genre_id IN " +
                        "(SELECT genre_id FROM film_genre WHERE film_id = ?)",
                (rs1, rowNum) -> genreDbStorage.makeGenreFromDb(rs1), id));
        List<Long> likedUserId = jdbcTemplate.queryForList("SELECT users_id FROM film_likes WHERE film_id = ?", Long.class, id);

        newFilm.setId(id);
        newFilm.setName(name);
        newFilm.setDescription(description);
        newFilm.setReleaseDate(releaseDate);
        newFilm.setDuration(duration);
        newFilm.setRate(rate);
        newFilm.setMpa(mpa);
        newFilm.setGenres(genre);
        newFilm.getLikedUserId().addAll(likedUserId);
        return newFilm;
    }


    private long getFilmIdFromFDb() {
        return jdbcTemplate.queryForObject("SELECT MAX(film_id) FROM film", Long.class);
    }
}
