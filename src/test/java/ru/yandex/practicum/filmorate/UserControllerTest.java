package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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


}