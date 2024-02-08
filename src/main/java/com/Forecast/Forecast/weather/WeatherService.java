package com.Forecast.Forecast.weather;


import com.Forecast.Forecast.weather.data.UnitGroup;
import com.Forecast.Forecast.weather.data.WeatherClient;
import com.Forecast.Forecast.weather.data.WeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
        String include = "hours,days";
        UnitGroup unitGroup = UnitGroup.metric;
        return weatherClient.getWeatherData(unitGroup, include, weatherClientApiKey, city);
    }
}
