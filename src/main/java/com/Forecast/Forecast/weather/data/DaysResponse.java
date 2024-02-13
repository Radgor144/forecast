package com.Forecast.Forecast.weather.data;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record DaysResponse(LocalDate datetime, List<HoursResponse> hours) {
}