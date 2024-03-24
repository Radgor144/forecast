package com.Forecast.Forecast.weather.data;

import lombok.Builder;

import java.util.List;

@Builder
public record WeatherData(String resolvedAddress, List<DaysResponse> days) {
}
