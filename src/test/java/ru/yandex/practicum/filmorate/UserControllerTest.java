package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void addUser() throws Exception {
        User user = new User(1, "mail@mail.ru", "dolore", "Nick Name", LocalDate.of(1946, 8, 20));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Nick Name"))
                .andExpect(jsonPath("$.login").value("dolore"))
                .andExpect(jsonPath("$.birthday").value("1946-08-20"));
        mockMvc.perform(get("/users/clear")
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void emptyName() throws Exception {
        User user = new User(1, "mail@mail.ru", "dolore", "", LocalDate.of(1946, 8, 20));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("dolore"));
        mockMvc.perform(get("/users/clear")
                .contentType(MediaType.APPLICATION_JSON));
    }

//    @Test
//    void emptyLogin() throws Exception {
//        User user = new User(1, "mail@mail.ru", "", "Nick Name", LocalDate.of(1946, 8, 20));
//        NestedServletException exception = assertThrows(NestedServletException.class,
//                () -> mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(user))
//
//                ));
//        String exceptionMessage = exception.getCause().getMessage();
//        assertEquals("логин не может быть пустым и содержать пробелы", exceptionMessage);
//    }

//    @Test
//    void emptyEmail() throws Exception {
//        User user = new User(1, "", "dolore", "Nick Name", LocalDate.of(1946, 8, 20));
//        NestedServletException exception = assertThrows(NestedServletException.class,
//                () -> mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(user))
//
//                ));
//        String exceptionMessage = exception.getCause().getMessage();
//        assertEquals("электронная почта не может быть пустой и должна содержать символ @", exceptionMessage);
//    }

    @Test
    void birthday() throws Exception {
        User user = new User(1, "mail@mail.ru", "dolore", "Nick Name", LocalDate.of(2024, 8, 20));
        NestedServletException exception = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))

                ));
        String exceptionMessage = exception.getCause().getMessage();
        assertEquals("дата рождения не может быть в будущем", exceptionMessage);
    }

    @Test
    void updateUser() throws Exception {
        User user = new User(1, "mail@mail.ru", "dolore", "Nick Name", LocalDate.of(1946, 8, 20));
        User updateUser = new User(1, "mail@yandex.ru", "doloreUpdate", "est adipisicing", LocalDate.of(1976, 9, 20));
        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
        );
        mockMvc.perform(put("/users")
                        .content(objectMapper.writeValueAsString(updateUser))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("est adipisicing"))
                .andExpect(jsonPath("$.login").value("doloreUpdate"))
                .andExpect(jsonPath("$.email").value("mail@yandex.ru"))
                .andExpect(jsonPath("$.birthday").value("1976-09-20"));
        mockMvc.perform(get("/users/clear")
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateUnknownUser() throws Exception {
        User user = new User(1, "mail@mail.ru", "dolore", "Nick Name", LocalDate.of(1946, 8, 20));
        User updateUser = new User(99, "mail@yandex.ru", "doloreUpdate", "est adipisicing", LocalDate.of(1976, 9, 20));
        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
        );
        NestedServletException exception = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(put("/users")
                        .content(objectMapper.writeValueAsString(updateUser))
                        .contentType(MediaType.APPLICATION_JSON)
                ));

        String exceptionMessage = exception.getCause().getMessage();
        assertEquals("такого пользователя не существует", exceptionMessage);
    }

//    @Test
//    void getAllUsers() throws Exception {
//        User user1 = new User(1, "mail@mail.ru", "dolore", "Nick Name", LocalDate.of(1946, 8, 20));
//        User user2 = new User(1, "mail@mail2.ru", "dolore", "Nick Name", LocalDate.of(1946, 8, 20));
//        User user3 = new User(1, "mail@mail3.ru", "dolore", "Nick Name", LocalDate.of(1946, 8, 20));
//        mockMvc.perform(post("/users")
//                .content(objectMapper.writeValueAsString(user1))
//                .contentType(MediaType.APPLICATION_JSON));
//        mockMvc.perform(post("/users")
//                .content(objectMapper.writeValueAsString(user2))
//                .contentType(MediaType.APPLICATION_JSON));
//        mockMvc.perform(post("/users")
//                .content(objectMapper.writeValueAsString(user3))
//                .contentType(MediaType.APPLICATION_JSON));
//        mockMvc.perform(get("/users")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().string("[{\"id\":1,\"email\":\"mail@mail.ru\",\"login\":\"dolore\"," +
//                        "\"name\":\"Nick Name\",\"birthday\":\"1946-08-20\"},{\"id\":2,\"email\":\"mail@mail2.ru\"," +
//                        "\"login\":\"dolore\",\"name\":\"Nick Name\",\"birthday\":\"1946-08-20\"},{\"id\":3,\"email\":" +
//                        "\"mail@mail3.ru\",\"login\":\"dolore\",\"name\":\"Nick Name\",\"birthday\":\"1946-08-20\"}]"));
//    }
}