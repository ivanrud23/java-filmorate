package ru.yandex.practicum.filmorate.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmRealiseDateValidator implements ConstraintValidator<FilmRealiseDate, LocalDate> {


    @Override
    public boolean isValid(LocalDate realiseDate, ConstraintValidatorContext constraintValidatorContext) {
        return realiseDate.isAfter(LocalDate.of(1895, 12, 28));
    }
}
