package com.Forecast.Forecast;

import java.time.LocalDate;
import java.util.List;

public record DaysResponse(LocalDate datetime, List<HoursResponse> hours) {
}