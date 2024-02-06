package com.Forecast.Forecast.weather;

import com.Forecast.Forecast.weather.data.WeatherData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ForecastController {

    private final WeatherService weatherService;

    @GetMapping("/forecast/{city}")
    public WeatherData getWeatherData(@PathVariable String city) {
        return weatherService.getWeatherData(city);
    }

}