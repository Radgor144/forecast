package com.Forecast.Forecast.weather;

import com.Forecast.Forecast.weather.data.WeatherData;
import com.Forecast.Forecast.weather.exceptions.WeatherDataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class ForecastController {

    private final WeatherService weatherService;

    @GetMapping("/forecast/{city}")
    public WeatherData getWeatherData(@PathVariable String city) {
        try {
            return weatherService.getWeatherData(city);
        } catch (WeatherDataNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Weather data not found", ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex);
        }
    }
}