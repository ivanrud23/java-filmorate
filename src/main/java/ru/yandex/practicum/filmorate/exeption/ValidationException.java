package ru.yandex.practicum.filmorate.exeption;

public class ValidationException extends RuntimeException {
    public ValidationException(String s) {
        super(s);
    }
}
