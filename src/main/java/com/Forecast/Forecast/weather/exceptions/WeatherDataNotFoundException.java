package com.Forecast.Forecast.weather.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WeatherDataNotFoundException extends RuntimeException {
    public WeatherDataNotFoundException(String message) {
        super(message);
    }
}