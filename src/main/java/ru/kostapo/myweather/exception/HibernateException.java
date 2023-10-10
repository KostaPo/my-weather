package ru.kostapo.myweather.exception;

public class HibernateException extends RuntimeException {
    public HibernateException(String message) {
        super(message);
    }
}