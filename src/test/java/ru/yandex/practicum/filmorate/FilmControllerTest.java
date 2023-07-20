package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    @Test
    void addFilm() throws Exception {
        Film film = new Film(1, "nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 25), 100);
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("nisi eiusmod"))
                .andExpect(jsonPath("$.description").value("adipisicing"))
                .andExpect(jsonPath("$.releaseDate").value("1967-03-25"))
                .andExpect(jsonPath("$.duration").value("100"));
        mockMvc.perform(get("/films/clear")
                .contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    void descriptionLength() throws Exception {
        Film film = new Film(1, "nisi eiusmod", "хппухшёчифхцарзнфмглтмтхябёквнзшыьлмтбшожмбнъвиинулоошбщъхрккоъ\" +\n" +
                "\"ёэйумбвбйзмизеьюцуюудзэблцизюаквзхееагшяйьшувхаюхсмаыцзьээюфэххрсювыкцкппастбблчепид\" +\n" +
                "\"ырэьхдкупфжиетмтхэоэххютцплфкдбеящтщртмяцяыъпзйвёыъэху", LocalDate.of(1967, 3, 25), 100);
        NestedServletException exception = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                ));
        String exceptionMessage = exception.getCause().getMessage();
        assertEquals("Превышена максимальная длина описания — 200 символов", exceptionMessage);
    }

    @Test
    void releaseDate() throws Exception {
        Film film = new Film(1, "nisi eiusmod", "adipisicing", LocalDate.of(1867, 3, 25), 100);
        NestedServletException exception = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                ));
        String exceptionMessage = exception.getCause().getMessage();
        assertEquals("дата релиза не может быть раньше 28 декабря 1895 года", exceptionMessage);
    }


    @Test
    void updateFilm() throws Exception {
        Film film = new Film(1, "nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 25), 100);
        Film updateFilm = new Film(1, "Film Updated", "New film update decription", LocalDate.of(1989, 4, 17), 190);
        mockMvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(film))
                .contentType(MediaType.APPLICATION_JSON)
        );
        mockMvc.perform(put("/films")
                        .content(objectMapper.writeValueAsString(updateFilm))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Film Updated"))
                .andExpect(jsonPath("$.description").value("New film update decription"))
                .andExpect(jsonPath("$.releaseDate").value("1989-04-17"))
                .andExpect(jsonPath("$.duration").value("190"));
        mockMvc.perform(get("/films/clear")
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateUnknownFilm() throws Exception {
        Film film = new Film(1, "nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 25), 100);
        Film updateFilm = new Film(99, "Film Updated", "New film update decription", LocalDate.of(1989, 4, 17), 190);
        mockMvc.perform(post("/films")
                .content(objectMapper.writeValueAsString(film))
                .contentType(MediaType.APPLICATION_JSON)
        );
        NestedServletException exception = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(put("/films")
                        .content(objectMapper.writeValueAsString(updateFilm))
                        .contentType(MediaType.APPLICATION_JSON)
                ));
        String exceptionMessage = exception.getCause().getMessage();
        assertEquals("такого фильма не существует", exceptionMessage);
    }

}