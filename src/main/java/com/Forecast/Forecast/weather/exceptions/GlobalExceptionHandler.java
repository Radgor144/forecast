package com.Forecast.Forecast.weather.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends RuntimeException {
    @ExceptionHandler(CustomHttpException.class)
    public ResponseEntity<ErrorResponse> handleCustomHttpException(CustomHttpException ex) {
        log.error("Error while connecting to weather api", ex);
        return ResponseEntity.status(ex.getHttpStatus()).body(new ErrorResponse("Error while connecting to weather client API.", ex.getMessage(), ex.getHttpStatus()));
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Error while user entered empty city name", ex);
        return ResponseEntity.status(400).body(new ErrorResponse("Error while user entered empty city name", ex.getMessage(), 400));
    }
}


