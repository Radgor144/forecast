package com.Forecast.Forecast.weather.exceptions;

public class WeatherDataNotFoundException extends RuntimeException {

    public WeatherDataNotFoundException(String message) {
        super(message);
    }
}
