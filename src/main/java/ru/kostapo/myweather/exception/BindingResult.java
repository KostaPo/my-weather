package ru.kostapo.myweather.exception;

import ru.kostapo.myweather.model.User;

import javax.validation.ConstraintViolation;
import java.util.*;

public class BindingResult {

    //TODO вывод ошибок по очереди

    private final Map<String, LinkedHashSet<String>> errors = new LinkedHashMap<>();

    public BindingResult(String field, String errorText) {
        errors.put(field, new LinkedHashSet<>());
        errors.get(field).add(errorText);
    }

    public BindingResult(Set<ConstraintViolation<User>> violations) {
        for(ConstraintViolation<User> violation : violations){
            String field = String.valueOf(violation.getPropertyPath());
            String errorText = violation.getMessage();
            if(!errors.containsKey(field)) {
                errors.put(field, new LinkedHashSet<>());
                errors.get(field).add(errorText);
            } else {
                errors.get(field).add(errorText);
            }
        }
    }

    public boolean hasErrors(String field) {
        return errors.containsKey(field);
    }

    public String getError(String field) {
        return errors.get(field).stream().findFirst().get();
    }
}
