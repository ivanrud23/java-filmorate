package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NoDataException;
import ru.yandex.practicum.filmorate.model.MpaRate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Data
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public MpaRate getMpaById(Long id) {
        if (!jdbcTemplate.queryForObject(String.format("SELECT EXISTS (SELECT 1 FROM mpa_rate WHERE mpa_rate_id = %d)", id), Boolean.class)) {
            throw new NoDataException("такого рейтинга не существует");
        }
        return jdbcTemplate.queryForObject("SELECT * FROM mpa_rate WHERE mpa_rate_id = ?", (rs, rowNum) -> makeMpaFromDb(rs), id);
    }

    @Override
    public List<MpaRate> getAllMpa() {
        return jdbcTemplate.query("SELECT * FROM mpa_rate", (rs, rowNum) -> makeMpaFromDb(rs));
    }

    public MpaRate makeMpaFromDb(ResultSet rs) throws SQLException {
        return new MpaRate(rs.getInt("mpa_rate_id"), rs.getString("name"));
    }
}
