package com.Forecast.Forecast;

import java.util.List;

public record WeatherData(String resolvedAddress, List<DaysResponse> days) {
}
