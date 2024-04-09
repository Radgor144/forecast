package com.Forecast.Forecast.weather;

import com.Forecast.Forecast.weather.data.WeatherData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.Forecast.Forecast.weather.config.WeatherClientCacheConfig.CACHENAME;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class ForecastController {

    private final WeatherService weatherService;

    // da sie znalezc miasto po kodzie pocztowym!
    @Cacheable(cacheNames = CACHENAME)
    @GetMapping("/forecast/{city}")
    public WeatherData getWeatherData(@PathVariable @Valid @NotBlank @Size(min = 2, max = 40) String city) {
//        log.info("input city: {}", city);
        return weatherService.getWeatherData(city);
    }

}