package com.Forecast.Forecast;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class WeatherService {

    private final WeatherClient weatherClient;

    private final String weatherClientApiKey;

    public WeatherService(WeatherClient weatherClient,
                          @Value("${api.key}") String weatherClientApiKey) {
        this.weatherClient = weatherClient;
        this.weatherClientApiKey = weatherClientApiKey;
    }

    public WeatherData getWeatherData(String city) {
        String include = "hours,days";
        String unitGroup = "metric"; // "metric", "us", "uk"

        return weatherClient.getWeatherData(unitGroup, include, weatherClientApiKey, city);
    }
}
