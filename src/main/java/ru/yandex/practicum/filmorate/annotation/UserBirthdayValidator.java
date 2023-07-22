package ru.yandex.practicum.filmorate.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class UserBirthdayValidator implements ConstraintValidator<UserBirthday, LocalDate> {


    @Override
    public boolean isValid(LocalDate userBirthday, ConstraintValidatorContext constraintValidatorContext) {
        return userBirthday.isBefore(LocalDate.now());
    }
}
