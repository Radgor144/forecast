package com.Forecast.Forecast.weather;


import com.Forecast.Forecast.weather.data.WeatherClient;
import com.Forecast.Forecast.weather.data.WeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.Forecast.Forecast.weather.data.WeatherConstants.INCLUDE;
import static com.Forecast.Forecast.weather.data.WeatherConstants.UNIT_GROUP;

@Service
public class WeatherService {

    private final WeatherClient weatherClient;

    private final String weatherClientApiKey;

    public WeatherService(WeatherClient weatherClient,
                          @Value("${weather.client.api-key:}") String weatherClientApiKey) {
        this.weatherClient = weatherClient;
        this.weatherClientApiKey = weatherClientApiKey;
    }

    public WeatherData getWeatherData(String city) {
        return weatherClient.getWeatherData(UNIT_GROUP, INCLUDE, weatherClientApiKey, city);
    }
}
