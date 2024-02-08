package com.Forecast.Forecast.weather.data;

import java.time.LocalTime;
import java.util.List;

public record HoursResponse(LocalTime datetime,
                            Double temp,
                            Double cloudcover,
                            Double precipprob,
                            Double humidity,
                            List<String> preciptype) {
}