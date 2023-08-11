package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NoDataException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component("userDbStorage")
@Data
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private int idCounter;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        if (jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class) == 0) {
            this.idCounter = 1;
        } else {
            this.idCounter = jdbcTemplate.queryForObject("SELECT MAX(users_id) FROM users", Integer.class) + 1;
        }
    }

    @Override
    public User addUser(User newUser) {
        newUser.setId(counter());
        addUserToDb(newUser);
        return newUser;
    }

    @Override
    public User updateUser(User newUser) {
        if (jdbcTemplate.queryForObject(String.format("SELECT COUNT(*) FROM users WHERE users_id = %d", newUser.getId()), Integer.class) == 0) {
            throw new NoDataException("такого пользователя не существует");
        }
        addUserToDb(newUser);
        return newUser;
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users", (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public Optional<User> getById(Long id) {
        return getUserFromDb(id);
    }

    @Override
    public void clearUsers() {

    }

    private void addUserToDb(User newUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }
        jdbcTemplate.execute(String.format("MERGE INTO users(users_id, email, login, name, birthday) VALUES (%d, '%s', '%s','%s', '%s');",
                newUser.getId(),
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday()));
    }

    public Optional<User> getUserFromDb(Long id) {
        if (jdbcTemplate.queryForObject(String.format("SELECT COUNT(*) FROM users WHERE users_id = %d", id), Integer.class) == 0) {
            throw new NoDataException("такого пользователя не существует");
        }
        User user = new User();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE users_id = ?", id);
        if (userRows.next()) {
            user.setId(id);
            user.setEmail(userRows.getString("email"));
            user.setLogin(userRows.getString("login"));
            user.setName(userRows.getString("name"));
            user.setBirthday(userRows.getDate("birthday").toLocalDate());
        }
        List<Long> friendsRows = jdbcTemplate.queryForList(String.format("SELECT user_id_2 FROM friendship WHERE user_id_1 = %d AND confirm = TRUE", id), Long.class);
        if (!friendsRows.isEmpty()) {
            user.getFriends().addAll(friendsRows);
        }
        return Optional.of(user);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        long id = rs.getLong("users_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return new User(id, email, login, name, birthday);
    }

    public int counter() {
        return idCounter++;
    }

    @Override
    public void addUsersToFriend(User user1, User user2) {
        boolean confirm = (jdbcTemplate.queryForObject(String.format("SELECT COUNT(*) FROM friendship WHERE user_id_1 = %d " +
                "AND user_id_2 = %d AND request = TRUE", user2.getId(), user1.getId()), Integer.class) != 0);
        jdbcTemplate.execute(String.format("INSERT INTO friendship VALUES(%d, %d, TRUE, TRUE)", user1.getId(), user2.getId()));
        jdbcTemplate.execute(String.format("INSERT INTO friendship VALUES(%d, %d, TRUE, %b)", user2.getId(), user1.getId(), confirm));
    }

    @Override
    public void removeUserFromFriends(User user1, User user2) {
        jdbcTemplate.execute(String.format("DELETE FROM friendship WHERE user_id_1 = %d AND user_id_2 = %d", user1.getId(), user2.getId()));
        jdbcTemplate.execute(String.format("MERGE INTO friendship VALUES(%d, %d, TRUE, FALSE)", user2.getId(), user1.getId()));
    }
}
