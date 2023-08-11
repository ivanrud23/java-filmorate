package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class UserDbStorageTest {

    private final ru.yandex.practicum.filmorate.storage.UserDbStorage userStorage;

    @Test
    public void testCreateFindUserById() {

        userStorage.addUser(new User(1, "mail@mail.ru", "sdf", "dfs", LocalDate.of(1946, 8, 20)));
        Optional<User> userOptional = userStorage.getUserFromDb(Long.valueOf(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", Long.valueOf(1))
                );
    }

    @Test
    public void testUpdateUserById() {

        userStorage.addUser(new User(1, "mail@mail.ru", "sdf", "dfs", LocalDate.of(1946, 8, 20)));
        userStorage.updateUser(new User(1, "Update_mail@mail.ru", "Update_login", "update_name", LocalDate.of(1941, 9, 21)));
        Optional<User> userOptional = userStorage.getUserFromDb(Long.valueOf(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", Long.valueOf(1)))
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "Update_mail@mail.ru"))
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "Update_login"))
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "update_name"))
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.of(1941, 9, 21)));
    }


}
