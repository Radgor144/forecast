package com.Forecast.Forecast.weather.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weather-client", configuration = WeatherClientConfiguration.class)
public interface WeatherClient {

    @GetMapping("/VisualCrossingWebServices/rest/services/timeline/{city}")
    WeatherData getWeatherData(
            @RequestParam("unitGroup") UnitGroup unitGroup,
            @RequestParam("include") String include,
            @RequestParam("key") String key,
            @PathVariable("city") String city
    );
}