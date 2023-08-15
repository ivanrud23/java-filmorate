package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NoDataException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Component
@Data
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(Long id) {
        if (!jdbcTemplate.queryForObject(String.format("SELECT EXISTS (SELECT 1 FROM genre WHERE genre_id = %d)", id), Boolean.class)) {
            throw new NoDataException("такого жанра не существует");
        }
        return jdbcTemplate.queryForObject("SELECT * FROM genre WHERE genre_id = ?", (rs, rowNum) -> makeGenreFromDb(rs), id);
    }

    @Override
    public List<Genre> getAllGenre() {
        return jdbcTemplate.query("SELECT * FROM genre", (rs, rowNum) -> makeGenreFromDb(rs));
    }

    public Genre makeGenreFromDb(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("genre_id"), rs.getString("name"));
    }
}
