package ru.kostapo.myweather.utils;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ValidationUtil {

    private static Validator validator;

    public static Validator getValidator(){
        if(validator == null) {
            ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
            validator = validatorFactory.getValidator();
            validatorFactory.close();
        }
        return validator;
    }
}
