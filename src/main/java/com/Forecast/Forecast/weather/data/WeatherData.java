package com.Forecast.Forecast.weather.data;

import java.util.List;

public record WeatherData(String resolvedAddress, List<DaysResponse> days) {
}
