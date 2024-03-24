package com.Forecast.Forecast.weather.fixtures;

import com.Forecast.Forecast.weather.data.DaysResponse;
import com.Forecast.Forecast.weather.data.HoursResponse;
import com.Forecast.Forecast.weather.data.WeatherData;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class WeatherDataFixture {

    public static WeatherData defaultWeatherData(String resolvedAddress) {
        return WeatherData.builder()
                .resolvedAddress(resolvedAddress)
                .days(defaultDays()).build();
    }

    private static List<DaysResponse> defaultDays() {
        return List.of(DaysResponse.builder()
                .datetime(LocalDate.of(2024, 2, 12))
                .hours(defaultHours())
                .build());
    }

    private static List<HoursResponse> defaultHours() {
        return IntStream.rangeClosed(0, 23)
                .mapToObj(WeatherDataFixture::defaultHourResponse)
                .toList();
    }

    private static HoursResponse defaultHourResponse(int hour) {
        return HoursResponse.builder()
                .datetime(LocalTime.of(hour, 0))
                .temp(generateRandomDouble(30))
                .cloudcover(generateRandomDouble(100))
                .precipprob(generateRandomDouble(100))
                .humidity(generateRandomDouble(100))
                .preciptype(null)
                .build();
    }

    private static double generateRandomDouble(int bound) {
        return ThreadLocalRandom.current().nextDouble(0, bound);
    }
}