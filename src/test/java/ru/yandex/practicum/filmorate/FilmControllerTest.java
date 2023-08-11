package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

//
//    @Test
//    void addFilm() throws Exception {
//        Film film = new Film(1, "nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 25), 100);
//        mockMvc.perform(post("/films")
//                        .content(objectMapper.writeValueAsString(film))
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").isNumber())
//                .andExpect(jsonPath("$.name").value("nisi eiusmod"))
//                .andExpect(jsonPath("$.description").value("adipisicing"))
//                .andExpect(jsonPath("$.releaseDate").value("1967-03-25"))
//                .andExpect(jsonPath("$.duration").value("100"));
//        mockMvc.perform(get("/films/clear")
//                .contentType(MediaType.APPLICATION_JSON));
//    }
//
//
//    @Test
//    void updateFilm() throws Exception {
//        Film film = new Film(1, "nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 25), 100);
//        Film updateFilm = new Film(1, "Film Updated", "New film update decription", LocalDate.of(1989, 4, 17), 190);
//        mockMvc.perform(post("/films")
//                .content(objectMapper.writeValueAsString(film))
//                .contentType(MediaType.APPLICATION_JSON)
//        );
//        mockMvc.perform(put("/films")
//                        .content(objectMapper.writeValueAsString(updateFilm))
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").isNumber())
//                .andExpect(jsonPath("$.name").value("Film Updated"))
//                .andExpect(jsonPath("$.description").value("New film update decription"))
//                .andExpect(jsonPath("$.releaseDate").value("1989-04-17"))
//                .andExpect(jsonPath("$.duration").value("190"));
//        mockMvc.perform(get("/films/clear")
//                .contentType(MediaType.APPLICATION_JSON));
//    }


}