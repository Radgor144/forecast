package com.Forecast.Forecast;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weather-client")
public interface WeatherClient {

    @GetMapping("/VisualCrossingWebServices/rest/services/timeline/{city}")
    WeatherData getWeatherData(
            @RequestParam("unitGroup") String unitGroup,
            @RequestParam("include") String include,
            @RequestParam("key") String key,
            @RequestParam("city") String city
    );
}
