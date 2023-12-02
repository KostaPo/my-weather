package ru.kostapo.myweather.utils;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ValidationUtil {

    private static Validator validator;

    public static Validator getValidator() {
        if (validator == null) {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                Class<?> expressionFactoryClass = classLoader.loadClass("javax.el.ExpressionFactory");
                if (expressionFactoryClass != null) {
                    ValidatorFactory validatorFactory = Validation.byDefaultProvider()
                            .configure()
                            .messageInterpolator(new ParameterMessageInterpolator())
                            .buildValidatorFactory();
                    validator = validatorFactory.getValidator();
                    validatorFactory.close();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return validator;
    }
}
