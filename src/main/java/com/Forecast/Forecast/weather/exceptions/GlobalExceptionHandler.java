package com.Forecast.Forecast.weather.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WeatherDataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleWeatherDataNotFoundException(WeatherDataNotFoundException ex) {
        return new ErrorResponse(ex.getMessage(), "Weather data not found", HttpStatus.NOT_FOUND.value());
    }
}


