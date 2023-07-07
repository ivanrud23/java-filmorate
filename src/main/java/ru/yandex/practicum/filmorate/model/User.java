package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private int id;
    @NotBlank(message = "email is mandatory")
    @Email(message = "email is invalid",regexp = "[a-zA-Z0-9]{1,20}")
    private String email;
    @NotBlank(message = "login is mandatory")
    private String login;
    private String name;
    private LocalDate birthday;

}
