package com.Forecast.Forecast.weather.exceptions;


import lombok.Getter;

@Getter
public class CustomHttpException extends RuntimeException {
    private final int httpStatus;
    public CustomHttpException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
