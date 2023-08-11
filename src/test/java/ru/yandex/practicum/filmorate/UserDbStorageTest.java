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
    public void testFindUserById() {

        userStorage.addUser(new User(1, "mail@mail.ru", "sdf", "dfs", LocalDate.of(1946, 8, 20)));
        Optional<User> userOptional = userStorage.getUserFromDb(Long.valueOf(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", Long.valueOf(1))
                );
    }
}
