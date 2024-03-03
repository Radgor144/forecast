package com.Forecast.Forecast.weather.exceptions;

public class CustomHttpException extends RuntimeException {
    private int httpStatus;
    public CustomHttpException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
