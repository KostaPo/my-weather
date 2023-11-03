package ru.kostapo.myweather.exception;

public class UniqConstraintViolationException extends RuntimeException {
    public UniqConstraintViolationException(String message) {
        super(message);
    }
}
