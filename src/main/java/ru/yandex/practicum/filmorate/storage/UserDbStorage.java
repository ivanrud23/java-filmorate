package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NoDataException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User newUser) {
        addUserToDb(newUser);
        return newUser;
    }

    @Override
    public User updateUser(User newUser) {
        if (!jdbcTemplate.queryForObject(String.format("SELECT EXISTS (SELECT 1 FROM users WHERE users_id = %d)", newUser.getId()), Boolean.class)) {
            throw new NoDataException("такого пользователя не существует");
        }
        updateUserToDb(newUser);
        return newUser;
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users", (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User getById(Long id) {
        return getUserFromDb(id);
    }

    private void addUserToDb(User newUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }
        jdbcTemplate.execute(String.format("INSERT INTO users(email, login, name, birthday) VALUES ('%s', '%s','%s', '%s');",
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday()));
        newUser.setId(getUserIdFromFDb());
    }

    private void updateUserToDb(User newUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }
        jdbcTemplate.execute(String.format("MERGE INTO users(users_id, email, login, name, birthday) VALUES (%d, '%s', '%s','%s', '%s');",
                newUser.getId(),
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday()));
        newUser.setId(getUserIdFromFDb());
    }

    private User getUserFromDb(Long id) {
        if (!jdbcTemplate.queryForObject(String.format("SELECT EXISTS (SELECT 1 FROM users WHERE users_id = %d)", id), Boolean.class)) {
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
        List<Long> friendsRows = jdbcTemplate.queryForList(String.format("SELECT user_id_2 FROM friendship WHERE user_id_1 = %d", id), Long.class);
        if (!friendsRows.isEmpty()) {
            user.getFriends().addAll(friendsRows);
        }
        return user;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        long id = rs.getLong("users_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return new User(id, email, login, name, birthday);
    }

    @Override
    public void addUsersToFriend(User user1, User user2) {
        jdbcTemplate.execute(String.format("INSERT INTO friendship VALUES(%d, %d, TRUE)", user1.getId(), user2.getId()));
    }

    @Override
    public void removeUserFromFriends(User user1, User user2) {
        jdbcTemplate.execute(String.format("DELETE FROM friendship WHERE user_id_1 = %d AND user_id_2 = %d", user1.getId(), user2.getId()));
    }

    public long getUserIdFromFDb() {
        return jdbcTemplate.queryForObject("SELECT MAX(users_id) FROM users", Long.class);
    }
}
