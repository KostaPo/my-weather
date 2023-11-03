package ru.kostapo.myweather.common;

import ru.kostapo.myweather.model.User;

import javax.validation.ConstraintViolation;
import java.util.*;

public class BindingResult {

    private final Map<String, List<String>> errors = new HashMap<>();

    public BindingResult(String field, String errorText) {
        errors.put(field, new ArrayList<>());
        errors.get(field).add(errorText);
    }

    public BindingResult(Set<ConstraintViolation<User>> violations) {
        for(ConstraintViolation<User> violation : violations){
            String field = String.valueOf(violation.getPropertyPath());
            String errorText = violation.getMessage();
            if(!errors.containsKey(field)) {
                errors.put(field, new ArrayList<>());
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
        return errors.get(field).get(0);
    }
}
