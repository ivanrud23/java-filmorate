package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
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
class FilmControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void addFilm() throws Exception {
        mockMvc.perform(post("/films")
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": \"100\"\n" +
                                "}")
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
    void emptyName() throws Exception {
        NestedServletException exception = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(post("/films")
                        .content("{\n" +
                                "  \"name\": \"\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": \"100\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                ));
        String exceptionMessage = exception.getCause().getMessage();
        assertEquals("Название не может быть пустым", exceptionMessage);
    }

    @Test
    void descriptionLength() throws Exception {
        NestedServletException exception = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(post("/films")
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"хппухшёчифхцарзнфмглтмтхябёквнзшыьлмтбшожмбнъвиинулоошбщъхрккоъ" +
                                "ёэйумбвбйзмизеьюцуюудзэблцизюаквзхееагшяйьшувхаюхсмаыцзьээюфэххрсювыкцкппастбблчепид" +
                                "ырэьхдкупфжиетмтхэоэххютцплфкдбеящтщртмяцяыъпзйвёыъэху\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": \"100\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                ));
        String exceptionMessage = exception.getCause().getMessage();
        assertEquals("Превышена максимальная длина описания — 200 символов", exceptionMessage);
    }

    @Test
    void releaseDate() throws Exception {
        NestedServletException exception = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(post("/films")
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1867-03-25\",\n" +
                                "  \"duration\": \"100\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                ));
        String exceptionMessage = exception.getCause().getMessage();
        assertEquals("дата релиза не может быть раньше 28 декабря 1895 года", exceptionMessage);
    }

    @Test
    void duration() throws Exception {
        NestedServletException exception = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(post("/films")
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": \"-1\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                ));
        String exceptionMessage = exception.getCause().getMessage();
        assertEquals("продолжительность фильма должна быть положительной", exceptionMessage);
    }

    @Test
    void updateFilm() throws Exception {
        mockMvc.perform(post("/films")
                .content("{\n" +
                        "  \"name\": \"nisi eiusmod\",\n" +
                        "  \"description\": \"adipisicing\",\n" +
                        "  \"releaseDate\": \"1967-03-25\",\n" +
                        "  \"duration\": \"100\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
        );
        mockMvc.perform(put("/films")
                        .content("{\"id\":1,\"name\":\"Film Updated\",\"releaseDate\":\"1989-04-17\",\"description\":" +
                                "\"New film update decription\",\"duration\":190,\"rate\":4}")
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
        mockMvc.perform(post("/films")
                .content("{\n" +
                        "  \"name\": \"nisi eiusmod\",\n" +
                        "  \"description\": \"adipisicing\",\n" +
                        "  \"releaseDate\": \"1967-03-25\",\n" +
                        "  \"duration\": \"100\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
        );
        NestedServletException exception = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(put("/films")
                        .content("{\"id\":99,\"name\":\"Film Updated\",\"releaseDate\":\"1989-04-17\",\"description\":\"New film update decription\",\"duration\":190,\"rate\":4}")
                        .contentType(MediaType.APPLICATION_JSON)
                ));
        String exceptionMessage = exception.getCause().getMessage();
        assertEquals("такого фильма не существует", exceptionMessage);
    }

    @Test
    void getAllFilms() throws Exception {
        mockMvc.perform(post("/films")
                .content("{\n" +
                        "  \"name\": \"nisi eiusmod_1\",\n" +
                        "  \"description\": \"adipisicing\",\n" +
                        "  \"releaseDate\": \"1967-03-25\",\n" +
                        "  \"duration\": \"100\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
        );
        mockMvc.perform(post("/films")
                .content("{\n" +
                        "  \"name\": \"nisi eiusmod_2\",\n" +
                        "  \"description\": \"adipisicing\",\n" +
                        "  \"releaseDate\": \"1967-03-25\",\n" +
                        "  \"duration\": \"100\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
        );
        mockMvc.perform(post("/films")
                .content("{\n" +
                        "  \"name\": \"nisi eiusmod_3\",\n" +
                        "  \"description\": \"adipisicing\",\n" +
                        "  \"releaseDate\": \"1967-03-25\",\n" +
                        "  \"duration\": \"100\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
        );
        mockMvc.perform(get("/films")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[{\"id\":1,\"name\":\"nisi eiusmod_1\",\"description\":" +
                        "\"adipisicing\",\"releaseDate\":\"1967-03-25\",\"duration\":100},{\"id\":2,\"name\":" +
                        "\"nisi eiusmod_2\",\"description\":\"adipisicing\",\"releaseDate\":\"1967-03-25\",\"duration\":" +
                        "100},{\"id\":3,\"name\":\"nisi eiusmod_3\",\"description\":\"adipisicing\",\"releaseDate\":" +
                        "\"1967-03-25\",\"duration\":100}]"));
    }
}