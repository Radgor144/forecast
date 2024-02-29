package com.Forecast.Forecast.weather.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class BadGatewayException extends Exception {
    public BadGatewayException(String message) {
        super(message);
    }
}
