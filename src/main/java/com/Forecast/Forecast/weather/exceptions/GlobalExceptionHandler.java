package com.Forecast.Forecast.weather.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends RuntimeException {

    @ExceptionHandler(CustomHttpException.class)
    public ErrorResponse handleCustomHttpException(CustomHttpException ex) {
        log.error("Error while connecting to weather api", ex);
        return new ErrorResponse("Error while connecting to weather client API.", ex.getMessage(), ex.getHttpStatus());
    }
}


