package com.Forecast.Forecast;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForecastController {

    private final WeatherService weatherService;

    @Autowired
    public ForecastController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }



    @GetMapping("/forecast/{city}")
    public ResponseEntity<WeatherData> getController(@PathVariable String city) {
        WeatherData weatherData = weatherService.getWeatherData(city);

        if (weatherData != null) {
            return ResponseEntity.ok(weatherData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}