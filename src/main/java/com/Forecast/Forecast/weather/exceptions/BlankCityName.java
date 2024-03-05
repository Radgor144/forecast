package com.Forecast.Forecast.weather.exceptions;

public class BlankCityName extends RuntimeException {

    final private int HttpStatus = 404;

    public BlankCityName(String message) {
        super(message);
    }

    public int getHttpStatus() {
        return HttpStatus;
    }
}
