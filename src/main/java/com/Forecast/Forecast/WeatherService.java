package com.Forecast.Forecast;

import org.springframework.beans.factory.annotation.Value;

public class WeatherService {

    private final WeatherClient weatherClient;

    private final String weatherClientApiKey;

    public WeatherService(WeatherClient weatherClient,
                          @Value("${weather.client.api-key:}") String weatherClientApiKey) {
        this.weatherClient = weatherClient;
        this.weatherClientApiKey = weatherClientApiKey;
    }

    public WeatherData getWeatherData(String city) {
        String include = "hours,days";
        return weatherClient.getWeatherData(include, weatherClientApiKey, city);
    }
}
