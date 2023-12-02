package ru.kostapo.myweather.exception;

public class OpenWeatherException extends RuntimeException{
    public OpenWeatherException(String message) {
        super(message);
    }
}
