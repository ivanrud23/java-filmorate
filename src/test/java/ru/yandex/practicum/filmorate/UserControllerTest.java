package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    //    @Autowired
//    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void addUser() throws Exception {
        mockMvc.perform(post("/users")
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}")
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
        mockMvc.perform(post("/users")
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("dolore"));
        mockMvc.perform(get("/users/clear")
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void emptyLogin() throws Exception {
        NestedServletException exception = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}")

                ));
        String exceptionMessage = exception.getCause().getMessage();
        assertEquals("логин не может быть пустым и содержать пробелы", exceptionMessage);
    }

    @Test
    void emptyEmail() throws Exception {
        NestedServletException exception = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}")

                ));
        String exceptionMessage = exception.getCause().getMessage();
        assertEquals("электронная почта не может быть пустой и должна содержать символ @", exceptionMessage);
    }

    @Test
    void birthday() throws Exception {
        NestedServletException exception = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"2024-08-20\"\n" +
                                "}")

                ));
        String exceptionMessage = exception.getCause().getMessage();
        assertEquals("дата рождения не может быть в будущем", exceptionMessage);
    }

    @Test
    void updateUser() throws Exception {
        mockMvc.perform(post("/users")
                .content("{\n" +
                        "  \"login\": \"dolore\",\n" +
                        "  \"name\": \"Nick Name\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"1946-08-20\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
        );
        mockMvc.perform(put("/users")
                        .content("{\n" +
                                "  \"login\": \"doloreUpdate\",\n" +
                                "  \"name\": \"est adipisicing\",\n" +
                                "  \"id\": 1,\n" +
                                "  \"email\": \"mail@yandex.ru\",\n" +
                                "  \"birthday\": \"1976-09-20\"\n" +
                                "}")
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
        mockMvc.perform(post("/users")
                .content("{\n" +
                        "  \"login\": \"dolore\",\n" +
                        "  \"name\": \"Nick Name\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"1946-08-20\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
        );
        NestedServletException exception = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(put("/users")
                        .content("{\n" +
                                "  \"login\": \"doloreUpdate\",\n" +
                                "  \"name\": \"est adipisicing\",\n" +
                                "  \"id\": 9999,\n" +
                                "  \"email\": \"mail@yandex.ru\",\n" +
                                "  \"birthday\": \"1976-09-20\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                ));

        String exceptionMessage = exception.getCause().getMessage();
        assertEquals("такого пользователя не существует", exceptionMessage);
    }

    @Test
    void getAllUsers() throws Exception {
        mockMvc.perform(post("/users")
                .content("{\n" +
                        "  \"login\": \"dolore\",\n" +
                        "  \"name\": \"Nick Name\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"1946-08-20\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(post("/users")
                .content("{\n" +
                        "  \"login\": \"dolore\",\n" +
                        "  \"name\": \"Nick Name\",\n" +
                        "  \"email\": \"mail@mail2.ru\",\n" +
                        "  \"birthday\": \"1946-08-20\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(post("/users")
                .content("{\n" +
                        "  \"login\": \"dolore\",\n" +
                        "  \"name\": \"Nick Name\",\n" +
                        "  \"email\": \"mail@mail3.ru\",\n" +
                        "  \"birthday\": \"1946-08-20\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[{\"id\":1,\"email\":\"mail@mail.ru\",\"login\":\"dolore\"," +
                        "\"name\":\"Nick Name\",\"birthday\":\"1946-08-20\"},{\"id\":2,\"email\":\"mail@mail2.ru\"," +
                        "\"login\":\"dolore\",\"name\":\"Nick Name\",\"birthday\":\"1946-08-20\"},{\"id\":3,\"email\":" +
                        "\"mail@mail3.ru\",\"login\":\"dolore\",\"name\":\"Nick Name\",\"birthday\":\"1946-08-20\"}]"));
    }
}