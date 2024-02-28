package com.Forecast.Forecast.weather.exceptions;

public class UnknownException extends RuntimeException {

    public UnknownException() {
        super("Unknown exception");
    }
}
