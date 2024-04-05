package com.Forecast.Forecast.weather;

import com.Forecast.Forecast.weather.data.WeatherData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.Forecast.Forecast.weather.WeatherClientCacheConfig.CacheName;

@RestController
@RequiredArgsConstructor
@Validated
public class ForecastController {

    private final WeatherService weatherService;

    // da sie znalezc miasto po kodzie pocztowym!
    @Cacheable(cacheNames = CacheName)
    @GetMapping("/forecast/{city}")
    public WeatherData getWeatherData(@PathVariable @Valid @NotBlank @Size(min = 2, max = 40) String city) {
        return weatherService.getWeatherData(city);
    }

}