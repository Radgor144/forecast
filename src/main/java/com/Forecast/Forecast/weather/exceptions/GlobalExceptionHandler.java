package com.Forecast.Forecast.weather.exceptions;

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
        // entitybody trzeba zwrocic, zeby byl kod taki sam (teraz jest 200)
        return ResponseEntity.status(ex.getHttpStatus()).body(new ErrorResponse("Error while connecting to weather client API.", ex.getMessage(), ex.getHttpStatus()));
    }
    @ExceptionHandler(BlankCityName.class)
    public ResponseEntity<ErrorResponse> handleBlankCityName(BlankCityName ex) {
        log.error("Error while user entered empty city name", ex);
        // entitybody trzeba zwrocic, zeby byl kod taki sam (teraz jest 200)
        return ResponseEntity.status(ex.getHttpStatus()).body(new ErrorResponse("Error while user entered empty city name", ex.getMessage(), ex.getHttpStatus()));
    }
}


