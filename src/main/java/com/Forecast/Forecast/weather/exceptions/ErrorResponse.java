package com.Forecast.Forecast.weather.exceptions;

public record ErrorResponse(String message, String reason, int codeHTTP) {

}
