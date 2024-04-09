package com.Forecast.Forecast.weather.data;

import com.Forecast.Forecast.weather.config.RetryerConfig;
import com.Forecast.Forecast.weather.config.WeatherClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weather-client", configuration = {WeatherClientConfiguration.class, RetryerConfig.class})
public interface WeatherClient {

    @GetMapping("/VisualCrossingWebServices/rest/services/timeline/{city}")
    WeatherData getWeatherData(
            @RequestParam("unitGroup") UnitGroup unitGroup,
            @RequestParam("include") String include,
            @RequestParam("key") String key,
            @PathVariable("city") String city
    );
}