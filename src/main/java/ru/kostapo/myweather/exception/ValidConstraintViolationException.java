package ru.kostapo.myweather.exception;

import lombok.Getter;
import lombok.Setter;
import ru.kostapo.myweather.model.User;

import javax.validation.ConstraintViolation;
import java.util.Set;

@Setter
@Getter
public class ValidConstraintViolationException extends RuntimeException {

    private final Set<ConstraintViolation<User>> constraintViolations;

    public ValidConstraintViolationException(String message, Set<ConstraintViolation<User>> constraintViolations) {
        super(message);
        this.constraintViolations = constraintViolations;
    }
}
