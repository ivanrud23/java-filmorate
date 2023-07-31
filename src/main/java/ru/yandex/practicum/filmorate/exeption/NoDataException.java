package ru.yandex.practicum.filmorate.exeption;

public class NoDataException extends RuntimeException {
    public NoDataException(String message) {
        super(message);
    }
}
