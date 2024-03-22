package com.Forecast.Forecast.weather.data;

import lombok.Builder;

import java.time.LocalTime;
import java.util.List;

@Builder
public record HoursResponse(LocalTime datetime,
                            Double temp,
                            Double cloudcover,
                            Double precipprob,
                            Double humidity,
                            List<String> preciptype) {
}
